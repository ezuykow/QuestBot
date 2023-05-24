package ru.coffeecoders.questbot.actions;

import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.*;
import ru.coffeecoders.questbot.exceptions.*;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.properties.PropertySeed;
import ru.coffeecoders.questbot.services.*;
import ru.coffeecoders.questbot.viewers.EndGameViewer;
import ru.coffeecoders.questbot.viewers.TasksViewer;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ezuykow
 */
@Component
public class SimpleMessageActions {

    private final TeamService teamService;
    private final PlayerService playerService;
    private final GlobalChatService globalChatService;
    private final GameService gameService;
    private final TaskService taskService;
    private final QuestionService questionService;
    private final MessageToDeleteService messageToDeleteService;
    private final EndGameViewer endGameViewer;
    private final TasksViewer tasksViewer;
    private final MessageSender msgSender;
    private final Map<String, PropertySeed> properties;

    public SimpleMessageActions(TeamService teamService, PlayerService playerService,
                                GlobalChatService globalChatService, GameService gameService, TaskService taskService, QuestionService questionService, EndGameViewer endGameViewer, MessageSender msgSender,
                                MessageToDeleteService messageToDeleteService, TasksViewer tasksViewer, Map<String, PropertySeed> properties) {
        this.teamService = teamService;
        this.playerService = playerService;
        this.globalChatService = globalChatService;
        this.gameService = gameService;
        this.taskService = taskService;
        this.questionService = questionService;
        this.endGameViewer = endGameViewer;
        this.msgSender = msgSender;
        this.messageToDeleteService = messageToDeleteService;
        this.tasksViewer = tasksViewer;
        this.properties = properties;
    }

    //-----------------API START-----------------

    /**
     * Принимает апдейт, в тексте которого - название создаваемой команды.
     * Если такой команды еще не существует, то создает новую команду и записывает
     * создавшего игрока в нее
     * @param update апдейт с именем новой команды в тексте
     * @author ezuykow
     */
    public void registerNewTeam(ExtendedUpdate update) {
        long chatId = update.getMessageChatId();
        if (update.hasMessageText()) {
            Team newTeam = new Team(
                    update.getMessageText(),
                    globalChatService.findById(chatId)
                            .orElseThrow(NonExistentChat::new).getCreatingGameName(),
                    0,
                    chatId
            );
            checkTeamForExistAndSave(update, newTeam);
        }
        saveToMessageToDelete(update);
        markUselessRegTeamMsgAsInactive(update);
    }

    /**
     * Записывает игрока по userId из апдейта в команду с именем {@code teamName}
     * @param update апдейт с id игрока
     * @param teamName имя выбранной команды
     * @author ezuykow
     */
    public void joinTeam(ExtendedUpdate update, String teamName) {
        long chatId = update.getMessageChatId();
        Player newPlayer = createNewPlayer(update.getMessageFromUserId(),
                globalChatService.findById(chatId).orElseThrow(NonExistentChat::new).getCreatingGameName(),
                teamName, chatId
        );
        saveToMessageToDelete(update);
        markUselessRegTeamMsgAsInactive(update);
        addPlayersWithTeam(newPlayer, update);
    }

    public void validateAnswer(long chatId, String text, int msgId, long senderId) {
        int taskNo = Integer.parseInt(text.substring(0, text.indexOf(" ")));
        Task targetTask = taskService.findActualTasksByChatId(chatId)
                .stream().filter(t -> t.getTaskNumber() == taskNo).findAny()
                .orElseThrow(NonExistentTask::new);
        Question question = questionService.findById(targetTask.getQuestionId())
                .orElseThrow(NonExistentQuestion::new);

        String answer = text.substring(text.indexOf(" ") + 1).trim();
        String rightAnswers = question.getAnswer();

        if (checkAnswer(answer, rightAnswers)) {
            acceptAnswer(senderId, targetTask, taskNo, msgId, chatId, question.getAdditional());
        } else {
            msgSender.sendReply(chatId, "Ответ неверный! (Возможно Вы не соблюдали формат ответа)", msgId);
        }
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void checkTeamForExistAndSave(ExtendedUpdate update, Team newTeam) {
        teamService.findByTeamName(newTeam.getTeamName())
                .ifPresentOrElse(
                        team -> msgSender.send(update.getMessageChatId(),
                                "Команда \"" + team.getTeamName() + "\" уже существует!"),
                        () -> saveNewTeam(newTeam, update)
                );
    }

    /**
     * @author ezuykow
     */
    private void addPlayersWithTeam(Player player, ExtendedUpdate update) {
        playerService.save(player);
        msgSender.send(update.getMessageChatId(),
                "Игрок @" + update.getUsernameFromMessage() + " вступил в команду \"" +
                        update.getMessageText() + "\"",
                new ReplyKeyboardRemove(true)
        );
    }

    /**
     * @author ezuykow
     */
    private Player createNewPlayer(long tgUserId, String gameName, String teamName, long chatId) {
        return new Player(tgUserId, gameName, teamName, chatId);
    }

    /**
     * @author ezuykow
     */
    private void saveNewTeam(Team newTeam, ExtendedUpdate update) {
        teamService.save(newTeam);
        msgSender.send(update.getMessageChatId(),
                "@" + update.getUsernameFromMessage() +
                        " создал команду \"" + newTeam.getTeamName() + "\"");
        joinTeam(update, newTeam.getTeamName());
    }

    /**
     * @author ezuykow
     */
    private void saveToMessageToDelete(ExtendedUpdate update) {
        messageToDeleteService.save(
                new MessageToDelete(
                        update.getMessageId(),
                        update.getMessageFromUserId(),
                        "REGTEAM",
                        update.getMessageChatId(),
                        false
                )
        );
    }

    /**
     * @author ezuykow
     */
    private void markUselessRegTeamMsgAsInactive(ExtendedUpdate update) {
        List<MessageToDelete> mtds = messageToDeleteService.findByUserId(update.getMessageFromUserId())
                .stream()
                .filter(m -> m.getChatId() == update.getMessageChatId())
                .filter(m -> m.getRelate_to().equals("REGTEAM"))
                .toList();
        mtds.forEach(m -> m.setActive(false));
        messageToDeleteService.saveAll(mtds);
    }

    /**
     * @author ezuykow
     */
    private void sendAcceptedMsg(long chatId, String teamName, int taskNo, int msgId, String additional) {
        String hat = "Команда \"" + teamName + "\" правильно ответила на вопрос № " + taskNo + " и зарабатывает 1 балл!";
        boolean additionalNotNeeded = gameService.findByName(globalChatService.findById(chatId)
                        .orElseThrow(NonExistentChat::new).getCreatingGameName())
                .orElseThrow(NonExistentGame::new).isAdditionWithTask();
        String text = additionalNotNeeded || additional == null
                ? hat
                : hat + "\n➕Дополнительная информация: " + additional;
        msgSender.sendReply(chatId, text, msgId);
    }

    /**
     * @author ezuykow
     */
    private void setQuestionsLastUsage(int questionId) {
        Question q = questionService.findById(questionId).orElseThrow(NonExistentQuestion::new);
        q.setLastUsage(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        questionService.save(q);
    }

    /**
     * @author ezuykow
     */
    private void checkTeamScoreAndTasksCount(long chatId, String teamName) {
        Team team = teamService.findByChatIdAndTeamName(chatId, teamName).orElseThrow(NonExistentTeam::new);
        int score = team.getScore() + 1;
        team.setScore(score);
        teamService.save(team);
        Game game = gameService.findByName(team.getGameName())
                .orElseThrow(NonExistentGame::new);
        int maxPerformedInGame = game.getMaxPerformedQuestionsCount();
        if (score == maxPerformedInGame) {
            endGameViewer.finishGameByPerformedTasks(chatId, teamName, score);
        } else {
            checkActualTasksCount(chatId, game);
        }
    }

    /**
     * @author ezuykow
     */
    private void checkActualTasksCount(long chatId, Game game) {
        List<Task> actualTasks = taskService.findActualTasksByChatId(chatId);
        List<Task> tasksToAdd = taskService.findByChatId(chatId)
                .stream().filter(t -> !t.isActual() && t.getPerformedTeamName() == null)
                .limit(game.getQuestionsCountToAdd())
                .toList();

        if (actualTasks.isEmpty() && tasksToAdd.isEmpty()) {
            endGameViewer.finishGameByQuestionsEnded(chatId);
        }
        if (actualTasks.size() == game.getMinQuestionsCountInGame()) {
            tasksToAdd.forEach(t -> t.setActual(true));
            taskService.saveAll(tasksToAdd);
        }
    }

    /**
     * @author ezuykow
     */
    private boolean checkAnswer(String answer, String rightAnswers) {
        String answerDesc = properties.get("answers.descriptor").getActualProperty();
        String[] answers = rightAnswers.split(answerDesc);
        for (String s : answers) {
            if (answer.equalsIgnoreCase(s.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @author ezuykow
     */
    private void acceptAnswer(long senderId, Task targetTask, int taskNo, int msgId, long chatId, String additional) {
        String teamName = playerService.findById(senderId)
                .orElseThrow(NonExistentPlayer::new).getTeamName();
        targetTask.setPerformedTeamName(teamName);
        targetTask.setActual(false);
        taskService.save(targetTask);
        setQuestionsLastUsage(targetTask.getQuestionId());
        sendAcceptedMsg(chatId, teamName, taskNo, msgId, additional);
        checkTeamScoreAndTasksCount(chatId, teamName);
        tasksViewer.showActualTasks(chatId);
    }
}

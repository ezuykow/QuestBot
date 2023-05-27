package ru.coffeecoders.questbot.actions;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.actions.commands.AdminsCommandsActions;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.entities.Task;
import ru.coffeecoders.questbot.entities.Team;
import ru.coffeecoders.questbot.exceptions.*;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.properties.PropertySeed;
import ru.coffeecoders.questbot.services.*;
import ru.coffeecoders.questbot.viewers.EndGameViewer;
import ru.coffeecoders.questbot.viewers.TasksViewer;
import ru.coffeecoders.questbot.viewers.TeamsViewer;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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
    private final EndGameViewer endGameViewer;
    private final TasksViewer tasksViewer;
    private final TeamsViewer teamsViewer;
    private final MessageSender msgSender;
    private final Map<String, PropertySeed> properties;
    private final AdminsCommandsActions adminsCommandsActions;

    public SimpleMessageActions(TeamService teamService, PlayerService playerService,
                                GlobalChatService globalChatService, GameService gameService, TaskService taskService,
                                QuestionService questionService, EndGameViewer endGameViewer, MessageSender msgSender,
                                TasksViewer tasksViewer, TeamsViewer teamsViewer, Map<String, PropertySeed> properties,
                                AdminsCommandsActions adminsCommandsActions) {
        this.teamService = teamService;
        this.playerService = playerService;
        this.globalChatService = globalChatService;
        this.gameService = gameService;
        this.taskService = taskService;
        this.questionService = questionService;
        this.endGameViewer = endGameViewer;
        this.msgSender = msgSender;
        this.tasksViewer = tasksViewer;
        this.teamsViewer = teamsViewer;
        this.properties = properties;
        this.adminsCommandsActions = adminsCommandsActions;
    }

    //-----------------API START-----------------

    /**
     * @author ezuykow
     */
    public void registerNewTeams(ExtendedUpdate update) {
        long chatId = update.getMessageChatId();
        int replyMsgId = update.getReplyToMessage().messageId();
        int thisMsgId = update.getMessageId();
        long senderUserId = update.getMessageFromUserId();

        if (update.hasMessageText()) {
            try {
                int teamsCount = Integer.parseInt(update.getMessageText().trim());
                String creatingGameName = globalChatService.findById(chatId)
                        .orElseThrow(NonExistentChat::new).getCreatingGameName();

                List<Team> newTeams = new ArrayList<>(teamsCount);
                for (int i = 1; i <= teamsCount; i++) {
                    newTeams.add(
                            new Team(
                                    "Team " + i,
                                    creatingGameName,
                                    0,
                                    chatId
                            )
                    );
                }
                teamService.saveAll(newTeams);

                msgSender.sendDelete(chatId, replyMsgId);
                msgSender.sendDelete(chatId, thisMsgId);

                teamsViewer.showEmptyTeamChooser(chatId);
            } catch (NumberFormatException e) {
                msgSender.sendDelete(chatId, replyMsgId);
                adminsCommandsActions.performRegTeamCmd(thisMsgId, chatId, senderUserId);
            }
        }
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

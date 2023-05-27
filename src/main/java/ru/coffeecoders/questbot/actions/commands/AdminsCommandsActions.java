package ru.coffeecoders.questbot.actions.commands;

import com.pengrad.telegrambot.model.User;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.entities.Team;
import ru.coffeecoders.questbot.exceptions.NonExistentCallbackQuery;
import ru.coffeecoders.questbot.exceptions.NonExistentChat;
import ru.coffeecoders.questbot.exceptions.NonExistentGame;
import ru.coffeecoders.questbot.managers.BlockingManager;
import ru.coffeecoders.questbot.managers.RestrictingManager;
import ru.coffeecoders.questbot.managers.commands.Command;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.services.*;
import ru.coffeecoders.questbot.validators.ChatAndUserValidator;
import ru.coffeecoders.questbot.validators.GameValidator;
import ru.coffeecoders.questbot.viewers.*;

import java.util.List;

@Component
public class AdminsCommandsActions {

    private final GamesViewer gamesViewer;
    private final QuestionsViewer questionsViewer;
    private final TasksViewer tasksViewer;
    private final BlockingManager blockingManager;
    private final RestrictingManager restrictingManager;
    private final GlobalChatService globalChatService;
    private final GameService gameService;
    private final TeamService teamService;
    private final TaskService taskService;
    private final PlayerService playerService;
    private final PrepareGameViewer prepareGameViewer;
    private final EndGameViewer endGameViewer;
    private final ChatAndUserValidator chatValidator;
    private final GameValidator gameValidator;
    private final TeamsViewer teamsViewer;
    private final MessageSender msgSender;
    private final Messages messages;

    private AdminsCommandsActions(GamesViewer gamesViewer, TasksViewer tasksViewer, MessageSender msgSender,
                                  QuestionsViewer questionsViewer, BlockingManager blockingManager,
                                  RestrictingManager restrictingManager, GlobalChatService globalChatService,
                                  GameService gameService, TeamService teamService, TaskService taskService,
                                  PlayerService playerService, PrepareGameViewer prepareGameViewer,
                                  EndGameViewer endGameViewer, ChatAndUserValidator chatValidator,
                                  GameValidator gameValidator, TeamsViewer teamsViewer, Messages messages)
    {
        this.gamesViewer = gamesViewer;
        this.tasksViewer = tasksViewer;
        this.msgSender = msgSender;
        this.questionsViewer = questionsViewer;
        this.blockingManager = blockingManager;
        this.restrictingManager = restrictingManager;
        this.globalChatService = globalChatService;
        this.gameService = gameService;
        this.teamService = teamService;
        this.taskService = taskService;
        this.playerService = playerService;
        this.prepareGameViewer = prepareGameViewer;
        this.endGameViewer = endGameViewer;
        this.chatValidator = chatValidator;
        this.gameValidator = gameValidator;
        this.teamsViewer = teamsViewer;
        this.messages = messages;
    }

    //-----------------API START-----------------

    /**
     * @author ezuykow
     */
    public void showMyCommands(long chatId) {
        msgSender.send(chatId, Command.MY_COMMANDS);
    }

    /**
     * @author ezuykow
     */
    public void performRegTeamCmd(int msgId, long chatId, long senderAdminId) {
        if (gameValidator.isGameCreating(chatId) && !gameValidator.isGameStarted(chatId)) {
            User senderAdmin = msgSender.getChatMember(chatId, senderAdminId);
            msgSender.sendForceReply(chatId, "@" + senderAdmin.username() + messages.enterTeamCount(),
                    msgId);
        }
        msgSender.sendDelete(chatId, msgId);
    }

    /**
     * Собирает сообщение из пар название команды-счет и передает его
     * в {@link MessageSender}
     * @param chatId id чата
     */
    public void showInfo(long chatId) {
        if (gameValidator.isGameStarted(chatId)) {
            List<Team> teams = teamService.findByChatId(chatId);
            GlobalChat chat = globalChatService.findById(chatId).orElseThrow(NonExistentChat::new);
            Game game = gameService.findByName(chat.getCreatingGameName())
                    .orElseThrow(NonExistentGame::new);
            msgSender.send(chatId, createInfoText(game, chat, teams));
        }
    }

    /**
     * Получает название текущей игры по chatId, передает в {@link MessageSender} список
     * актуальных вопросов
     * @param chatId id чата
     * @author anna
     * @Redact: ezuykow
     */
    public void showQuestions(long chatId) {
        if (gameValidator.isGameStarted(chatId)) {
            tasksViewer.showActualTasks(chatId);
        } else {
            msgSender.send(chatId, messages.haventStartedGame());
        }
    }

    /**
     * Блокирует чат, ограничивает членов и вызывает {@link GamesViewer#viewGames}
     * @param senderAdminId id админа, который ввел команду /showgames
     * @param chatId id чата, в котором была введена команда
     * @author ezuykow
     */
    public void performShowGamesCmd(long senderAdminId, long chatId) {
        blockAndRestrictChat(chatId, senderAdminId, messages.startGamesView());
        gamesViewer.viewGames(chatId);
    }

    /**
     * Блокирует чат, ограничивает членов и вызывает {@link QuestionsViewer#viewQuestions}
     * @param senderAdminId id админа, который ввел команду /showquestion
     * @param chatId id чата, в котором была введена команда
     * @author ezuykow
     */
    public void performShowQuestionsCmd(long senderAdminId, long chatId) {
        blockAndRestrictChat(chatId, senderAdminId, messages.startQuestionView());
        questionsViewer.viewQuestions(chatId);
    }

    /**
     * Если этот чат глобальный, то удаляет его из БД, иначе предупреждает, что нельзя удалить
     * @param chatId id чата
     * @author ezuykow
     */
    public void performDeleteChatCmd(long chatId) {
        if (chatValidator.isGlobalChat(chatId)) {
            globalChatService.deleteById(chatId);
            msgSender.send(chatId, messages.chatNotInGame());
            msgSender.sendLeaveChat(chatId);
        } else {
            msgSender.send(chatId, messages.cmdForGlobalChat());
        }
    }

    /**
     * Вызывает {@link PrepareGameViewer#requestGameName} с теми же параметрами, если чат не админский,
     * иначе - сообщение
     * @param senderAdminId id админа, вызвавшего команду
     * @param chatId id чата
     * @author ezuykow
     */
    public void performPrepareGameCmd(long senderAdminId, long chatId) {
        final String adminUsername = "@" + msgSender.getChatMember(chatId, senderAdminId).username();
        if (chatValidator.isGlobalChat(chatId)) {
            if (globalChatService.findById(chatId).orElseThrow(NonExistentCallbackQuery::new)
                    .getCreatingGameName() == null) {
                prepareGameViewer.requestGameName(senderAdminId, adminUsername, chatId);
            }
        } else {
            msgSender.send(chatId, messages.cmdForGlobalChat() + ", " + adminUsername);
        }
    }

    /**
     * Прерывает подготовку игры в чате
     * @param senderAdminId id инициировавшего админа
     * @param chatId id чата
     * @author ezuykow
     */
    public void performDropPrepareGameCmd(long senderAdminId, long chatId) {
        final String adminUsername = "@" + msgSender.getChatMember(chatId, senderAdminId).username();
        if (gameValidator.isGameCreating(chatId)) {
            msgSender.send(chatId, adminUsername + messages.prepareInterrupted());
            dropPrepareGame(chatId);
        }
    }

    /**
     * Запускает подготовленную игру
     * @param senderAdminId id инициировавшего админа
     * @param chatId id чата
     * @author ezuykow
     */
    public void performStartGameCmd(long senderAdminId, long chatId) {
        final String adminUsername = "@" + msgSender.getChatMember(chatId, senderAdminId).username();
        if (gameValidator.isGameCreating(chatId)) {
            msgSender.send(chatId, adminUsername + messages.gameStarted());
            startGame(chatId);
        }
    }

    /**
     * Завершает игру принудительно
     * @param senderAdminId id инициировавшего админа
     * @param chatId id чата
     * @author ezuykow
     */
    public void performDropGameCmd(long senderAdminId, long chatId) {
        final String adminUsername = "@" + msgSender.getChatMember(chatId, senderAdminId).username();
        if (gameValidator.isGameStarted(chatId)) {
            endGameViewer.finishGameByAdminsCmd(chatId, adminUsername);
        }
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private String createInfoText(Game game, GlobalChat chat, List<Team> teams) {
        StringBuilder sb = new StringBuilder();
        sb.append("\uD83C\uDFB2Информация об игре\uD83C\uDFB2\n\n");
        sb.append("⏰ Оставшееся время - ").append(game.getMaxTimeMinutes() - chat.getMinutesSinceStart())
                .append(" минут\n\n");
        teams.forEach( t ->
                sb.append("\uD83D\uDC65 Команда \"").append(t.getTeamName()).append("\" - ")
                        .append(t.getScore()).append(" очков;\n")
        );
        return sb.toString();
    }

    /**
     * @author ezuykow
     */
    private void blockAndRestrictChat(long chatId, long senderAdminId, String cause) {
        blockingManager.blockAdminChatByAdmin(chatId, senderAdminId, cause);
        restrictingManager.restrictMembers(chatId, senderAdminId);
    }

    /**
     * @author ezuykow
     */
    private void dropPrepareGame(long chatId) {
        GlobalChat chat = globalChatService.findById(chatId).orElseThrow(NonExistentChat::new);
        teamsViewer.deleteShowedTeamsChooser(chatId);
        chat.setCreatingGameName(null);
        globalChatService.save(chat);
        taskService.deleteAllByChatId(chatId);
        playerService.deleteAllByChatId(chatId);
        teamService.deleteAllByChatId(chatId);
    }

    /**
     * @author ezuykow
     */
    private void startGame(long chatId) {
        GlobalChat chat = globalChatService.findById(chatId).orElseThrow(NonExistentChat::new);
        chat.setGameStarted(true);
        globalChatService.save(chat);

        Game game = gameService.findByName(chat.getCreatingGameName()).orElseThrow(NonExistentGame::new);

        msgSender.send(chatId,
                String.format(messages.gameStartedHint(),
                        game.getGameName(),
                        game.getMaxTimeMinutes(),
                        game.getMaxPerformedQuestionsCount()));

        tasksViewer.createAndSendTasksMsg(chatId, game.getStartCountTasks());
        teamsViewer.deleteShowedTeamsChooser(chatId);
        teamsViewer.sendCommandsToAdmins(chatId);
    }
}

package ru.coffeecoders.questbot.actions.commands;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.exceptions.NonExistentCallbackQuery;
import ru.coffeecoders.questbot.exceptions.NonExistentChat;
import ru.coffeecoders.questbot.exceptions.NonExistentGame;
import ru.coffeecoders.questbot.managers.ApplicationShutdownManager;
import ru.coffeecoders.questbot.managers.BlockingManager;
import ru.coffeecoders.questbot.managers.RestrictingManager;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.services.*;
import ru.coffeecoders.questbot.validators.ChatAndUserValidator;
import ru.coffeecoders.questbot.validators.GameValidator;
import ru.coffeecoders.questbot.viewers.GamesViewer;
import ru.coffeecoders.questbot.viewers.PrepareGameViewer;
import ru.coffeecoders.questbot.viewers.QuestionsViewer;
import ru.coffeecoders.questbot.viewers.TasksViewer;

@Component
public class AdminsCommandsActions {

    private final GamesViewer gamesViewer;
    private final QuestionsViewer questionsViewer;
    private final TasksViewer tasksViewer;
    private final BlockingManager blockingManager;
    private final RestrictingManager restrictingManager;
    private final ApplicationShutdownManager applicationShutdownManager;
    private final GlobalChatService globalChatService;
    private final GameService gameService;
    private final TeamService teamService;
    private final TaskService taskService;
    private final PlayerService playerService;
    private final PrepareGameViewer prepareGameViewer;
    private final ChatAndUserValidator chatValidator;
    private final GameValidator gameValidator;
    private final MessageSender msgSender;
    private final Messages messages;

    private AdminsCommandsActions(GamesViewer gamesViewer, TasksViewer tasksViewer, MessageSender msgSender, QuestionsViewer questionsViewer,
                                  ApplicationShutdownManager applicationShutdownManager,
                                  BlockingManager blockingManager, RestrictingManager restrictingManager,
                                  GlobalChatService globalChatService, GameService gameService, TeamService teamService,
                                  TaskService taskService, PlayerService playerService,
                                  PrepareGameViewer prepareGameViewer, ChatAndUserValidator chatValidator,
                                  GameValidator gameValidator, Messages messages)
    {
        this.gamesViewer = gamesViewer;
        this.tasksViewer = tasksViewer;
        this.msgSender = msgSender;
        this.questionsViewer = questionsViewer;
        this.applicationShutdownManager = applicationShutdownManager;
        this.blockingManager = blockingManager;
        this.restrictingManager = restrictingManager;
        this.globalChatService = globalChatService;
        this.gameService = gameService;
        this.teamService = teamService;
        this.taskService = taskService;
        this.playerService = playerService;
        this.prepareGameViewer = prepareGameViewer;
        this.chatValidator = chatValidator;
        this.gameValidator = gameValidator;
        this.messages = messages;
    }

    //-----------------API START-----------------

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
     * Вызывает {@link MessageSender#sendStopBot} и {@link ApplicationShutdownManager#stopBot}
     * @author ezuykow
     */
    public void performStopBotCmd() {
        msgSender.sendStopBot();
        applicationShutdownManager.stopBot();
    }

    //-----------------API END-----------------

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
    }
}

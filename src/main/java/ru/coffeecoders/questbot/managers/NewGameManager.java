package ru.coffeecoders.questbot.managers;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.actions.newgame.NewGameActions;
import ru.coffeecoders.questbot.actions.newgame.utils.NewGameUtils;
import ru.coffeecoders.questbot.entities.NewGameCreatingState;
import ru.coffeecoders.questbot.logs.LogSender;
import ru.coffeecoders.questbot.messages.Messages;

/**
 * @author ezuykow
 */
@Component
public class NewGameManager {

    private final NewGameActions actions;
    private final NewGameUtils utils;
    private final BlockingManager blockingManager;
    private final RestrictingManager restrictingManager;
    private final Messages messages;
    private final LogSender logger;

    public NewGameManager(NewGameActions actions, NewGameUtils utils, BlockingManager blockingManager,
                          RestrictingManager restrictingManager, Messages messages, LogSender logger) {
        this.actions = actions;
        this.utils = utils;
        this.blockingManager = blockingManager;
        this.restrictingManager = restrictingManager;
        this.messages = messages;
        this.logger = logger;
    }

    //-----------------API START-----------------

    /**
     * Вызывает {@link NewGameManager#blockAndRestrictChat} и {@link NewGameActions#createNewGameCreatingState}
     * @param senderAdminId id админа, инициировавшего создание игры
     * @param chatId id чата
     * @author ezuykow
     */
    public void startCreatingGame(long senderAdminId, long chatId) {
        logger.warn("Запускаю создание новой игры");
        blockAndRestrictChat(chatId, senderAdminId);
        actions.createNewGameCreatingState(chatId);
    }

    /**
     * В зависимости от ожидаемого свойства новой игры вызывает соответствующий метод
     * {@link NewGameActions}
     * @param chatId id чата
     * @param text текст ответа
     * @param msgId id сообщения с ответом
     * @author ezuykow
     */
    public void manageNewGamePart(long chatId, String text, int msgId) {
        NewGameCreatingState state = utils.getNewGameCreatingState(chatId);
        if (state.getGameName() == null) {
            actions.validateGameNameToStateAndRequestNextPart(chatId, state, text, msgId);
        } else if (state.getMaxQuestionsCount() == null) {
            actions.validateMaxQuestionsCountToStateAndRequestNextPart(chatId, state, text, msgId);
        } else if (state.getStartCountTasks() == null) {
            actions.validateStartCountTaskToStateAndRequestNextPart(chatId, state, text, msgId);
        } else if (state.getMaxPerformedQuestionsCount() == null) {
            actions.validateMaxPerformedQuestionsCountToStateAndRequestNextPart(chatId, state, text, msgId);
        } else if (state.getMinQuestionsCountInGame() == null) {
            actions.validateMinQuestionsCountInGameAndRequestNextPart(chatId, state, text, msgId);
        } else if (state.getQuestionsCountToAdd() == null) {
            actions.validateQuestionsCountToAddAndRequestNextPart(chatId, state, text, msgId);
        } else {
            actions.validateMaxTimeMinutesToStateAmdSaveNewGame(chatId, state, text, msgId);
        }
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void blockAndRestrictChat(long chatId, long senderAdminId) {
        blockingManager.blockAdminChatByAdmin(chatId, senderAdminId, messages.startGameCreating());
        restrictingManager.restrictMembers(chatId, senderAdminId);
    }
}

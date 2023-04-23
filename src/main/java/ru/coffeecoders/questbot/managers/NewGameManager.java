package ru.coffeecoders.questbot.managers;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.actions.newgame.NewGameActions;
import ru.coffeecoders.questbot.actions.newgame.utils.NewGameUtils;
import ru.coffeecoders.questbot.entities.NewGameCreatingState;

/**
 * @author ezuykow
 */
@Component
public class NewGameManager {

    private final NewGameActions actions;
    private final NewGameUtils utils;
    private final BlockingManager blockingManager;
    private final RestrictingManager restrictingManager;
    private final Environment env;

    public NewGameManager(NewGameActions actions, NewGameUtils utils, BlockingManager blockingManager,
                          RestrictingManager restrictingManager, Environment env) {
        this.actions = actions;
        this.utils = utils;
        this.blockingManager = blockingManager;
        this.restrictingManager = restrictingManager;
        this.env = env;
    }

    //-----------------API START-----------------

    public void startCreatingGame(long senderAdminId, long chatId) {
        blockAndRestrictChat(chatId, senderAdminId);
        actions.createNewGameCreatingState(chatId);
    }

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

    private void blockAndRestrictChat(long chatId, long senderAdminId) {
        blockingManager.blockAdminChatByAdmin(chatId, senderAdminId, env.getProperty("messages.admins.startGameCreating"));
        restrictingManager.restrictMembers(chatId, senderAdminId);
    }
}

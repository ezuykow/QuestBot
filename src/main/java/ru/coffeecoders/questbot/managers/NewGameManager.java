package ru.coffeecoders.questbot.managers;

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

    public NewGameManager(NewGameActions actions, NewGameUtils utils, BlockingManager blockingManager,
                          RestrictingManager restrictingManager) {
        this.actions = actions;
        this.utils = utils;
        this.blockingManager = blockingManager;
        this.restrictingManager = restrictingManager;
    }

    public void startCreatingGame(long senderAdminId, long chatId) {
        blockingManager.blockAdminChatByAdmin(chatId, senderAdminId);
        restrictingManager.restrictMembers(chatId, senderAdminId);
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
}

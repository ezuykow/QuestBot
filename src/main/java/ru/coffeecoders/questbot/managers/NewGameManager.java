package ru.coffeecoders.questbot.managers;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.actions.NewGameActions;
import ru.coffeecoders.questbot.entities.NewGameCreatingState;
import ru.coffeecoders.questbot.utils.BlockAdminChat;

/**
 * @author ezuykow
 */
@Component
public class NewGameManager {

    private enum NewGamePartType {
        GAME_NAME,
        START_COUNT_TASKS,
        MAX_QUESTIONS_COUNT,
        MAX_PERFORMED_QUESTIONS_COUNT,
        MIN_QUESTIONS_COUNT_IN_GAME,
        QUESTIONS_COUNT_TO_ADD,
        MAX_TIME_MINUTES
    }

    private final NewGameActions actions;
    private final BlockAdminChat blockAdminChat;

    public NewGameManager(NewGameActions actions, BlockAdminChat blockAdminChat) {
        this.actions = actions;
        this.blockAdminChat = blockAdminChat;
    }

    public void startCreatingGame(long senderAdminId, long chatId) {
        blockAdminChat.validateAndBlockAdminChatByAdmin(chatId, senderAdminId);
        actions.createNewGameCreatingState(chatId);
    }

    public void manageNewGamePart(long chatId, String text, int msgId) {
        NewGameCreatingState state = actions.getNewGameCreatingState(chatId);
        switch (getExpectedNewGamePartType(state)) {
            case GAME_NAME ->
                    actions.addGameNameToStateAndRequestNextPart(chatId, state, text, msgId);
            case MAX_QUESTIONS_COUNT ->
                    actions.addMaxQuestionsCountToStateAndRequestNextPart(chatId, state, text, msgId);
            case START_COUNT_TASKS ->
                    actions.addStartCountTaskToStateAndRequestNextPart(chatId, state, text, msgId);
            case MAX_PERFORMED_QUESTIONS_COUNT ->
                    actions.addMaxPerformedQuestionsCountToStateAndRequestNextPart(chatId, state, text, msgId);
            case MIN_QUESTIONS_COUNT_IN_GAME ->
                actions.addMinQuestionsCountInGameAndRequestNextPart(chatId, state, text, msgId);
        }
    }

    private NewGamePartType getExpectedNewGamePartType(NewGameCreatingState state) {
        if (state.getGameName() == null) {
            return NewGamePartType.GAME_NAME;
        }
        if (state.getMaxQuestionsCount() == null) {
            return NewGamePartType.MAX_QUESTIONS_COUNT;
        }
        if (state.getStartCountTasks() == null) {
            return NewGamePartType.START_COUNT_TASKS;
        }
        if (state.getMaxPerformedQuestionsCount() == null) {
            return NewGamePartType.MAX_PERFORMED_QUESTIONS_COUNT;
        }
        if (state.getMinQuestionsCountInGame() == null) {
            return NewGamePartType.MIN_QUESTIONS_COUNT_IN_GAME;
        }
        if (state.getQuestionsCountToAdd() == null) {
            return NewGamePartType.QUESTIONS_COUNT_TO_ADD;
        }
        return NewGamePartType.MAX_TIME_MINUTES;
    }
}

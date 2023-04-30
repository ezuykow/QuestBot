package ru.coffeecoders.questbot.managers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.actions.newgame.NewGameActions;
import ru.coffeecoders.questbot.actions.newgame.utils.NewGameUtils;
import ru.coffeecoders.questbot.entities.NewGameCreatingState;
import ru.coffeecoders.questbot.logs.LogSender;
import ru.coffeecoders.questbot.messages.Messages;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author ezuykow
 */
@ExtendWith(MockitoExtension.class)
class NewGameManagerTest {

    @Mock
    private NewGameActions actions;
    @Mock
    private NewGameUtils utils;
    @Mock
    private BlockingManager blockingManager;
    @Mock
    private RestrictingManager restrictingManager;
    @Mock
    private Messages messages;
    @Mock
    private LogSender logger;

    @InjectMocks
    private NewGameManager newGameManager;

    @Test
    public void shouldCallActionsMethodThenCalledStartCreatingGame() {
        long senderAdminId = 1L;
        long chatId = 10L;

        doNothing().when(logger).warn(anyString());
        when(messages.startGameCreating()).thenReturn("message");
        doNothing().when(blockingManager).blockAdminChatByAdmin(chatId, senderAdminId, "message");
        doNothing().when(restrictingManager).restrictMembers(eq(chatId), eq(senderAdminId));
        doNothing().when(actions).createNewGameCreatingState(eq(chatId));

        newGameManager.startCreatingGame(senderAdminId, chatId);
        verify(actions).createNewGameCreatingState(chatId);
    }

    @Test
    public void shouldCallValidateGameNameMethodWhenCalledManageNewGamePart() {
        long chatId = 1L;
        String text = "";
        int msgId = 1;
        NewGameCreatingState state = new NewGameCreatingState(chatId,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        when(utils.getNewGameCreatingState(chatId)).thenReturn(state);
        doNothing().when(actions).validateGameNameToStateAndRequestNextPart(chatId, state, "", msgId);

        newGameManager.manageNewGamePart(chatId, text, msgId);
        verify(actions).validateGameNameToStateAndRequestNextPart(chatId, state, "", msgId);
    }

    @Test
    public void shouldCallValidateMaxQuestionCountMethodWhenCalledManageNewGamePart() {
        long chatId = 1L;
        String text = "";
        int msgId = 1;
        NewGameCreatingState state = new NewGameCreatingState(chatId,
                "not_null",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        when(utils.getNewGameCreatingState(chatId)).thenReturn(state);
        doNothing().when(actions).validateMaxQuestionsCountToStateAndRequestNextPart(chatId, state, "", msgId);

        newGameManager.manageNewGamePart(chatId, text, msgId);
        verify(actions).validateMaxQuestionsCountToStateAndRequestNextPart(chatId, state, "", msgId);
    }

    @Test
    public void shouldCallValidateStartCountTaskMethodWhenCalledManageNewGamePart() {
        long chatId = 1L;
        String text = "";
        int msgId = 1;
        NewGameCreatingState state = new NewGameCreatingState(chatId,
                "not_null",
                null,
                null,
                10,
                null,
                null,
                null,
                null,
                null);

        when(utils.getNewGameCreatingState(chatId)).thenReturn(state);
        doNothing().when(actions).validateStartCountTaskToStateAndRequestNextPart(chatId, state, "", msgId);

        newGameManager.manageNewGamePart(chatId, text, msgId);
        verify(actions).validateStartCountTaskToStateAndRequestNextPart(chatId, state, "", msgId);
    }

    @Test
    public void shouldCallValidateMaxPerformedQuestionsCountMethodWhenCalledManageNewGamePart() {
        long chatId = 1L;
        String text = "";
        int msgId = 1;
        NewGameCreatingState state = new NewGameCreatingState(chatId,
                "not_null",
                null,
                5,
                10,
                null,
                null,
                null,
                null,
                null);

        when(utils.getNewGameCreatingState(chatId)).thenReturn(state);
        doNothing().when(actions).validateMaxPerformedQuestionsCountToStateAndRequestNextPart(chatId, state, "", msgId);

        newGameManager.manageNewGamePart(chatId, text, msgId);
        verify(actions).validateMaxPerformedQuestionsCountToStateAndRequestNextPart(chatId, state, "", msgId);
    }

    @Test
    public void shouldCallValidateMinQuestionsCountMethodWhenCalledManageNewGamePart() {
        long chatId = 1L;
        String text = "";
        int msgId = 1;
        NewGameCreatingState state = new NewGameCreatingState(chatId,
                "not_null",
                null,
                5,
                10,
                6,
                null,
                null,
                null,
                null);

        when(utils.getNewGameCreatingState(chatId)).thenReturn(state);
        doNothing().when(actions).validateMinQuestionsCountInGameAndRequestNextPart(chatId, state, "", msgId);

        newGameManager.manageNewGamePart(chatId, text, msgId);
        verify(actions).validateMinQuestionsCountInGameAndRequestNextPart(chatId, state, "", msgId);
    }

    @Test
    public void shouldCallValidateQuestionsCountToAddMethodWhenCalledManageNewGamePart() {
        long chatId = 1L;
        String text = "";
        int msgId = 1;
        NewGameCreatingState state = new NewGameCreatingState(chatId,
                "not_null",
                null,
                5,
                10,
                6,
                3,
                null,
                null,
                null);

        when(utils.getNewGameCreatingState(chatId)).thenReturn(state);
        doNothing().when(actions).validateQuestionsCountToAddAndRequestNextPart(chatId, state, "", msgId);

        newGameManager.manageNewGamePart(chatId, text, msgId);
        verify(actions).validateQuestionsCountToAddAndRequestNextPart(chatId, state, "", msgId);
    }

    @Test
    public void shouldCallValidateMaxTimeMinutesMethodWhenCalledManageNewGamePart() {
        long chatId = 1L;
        String text = "";
        int msgId = 1;
        NewGameCreatingState state = new NewGameCreatingState(chatId,
                "not_null",
                null,
                5,
                10,
                6,
                3,
                2,
                null,
                null);

        when(utils.getNewGameCreatingState(chatId)).thenReturn(state);
        doNothing().when(actions).validateMaxTimeMinutesToStateAmdSaveNewGame(chatId, state, "", msgId);

        newGameManager.manageNewGamePart(chatId, text, msgId);
        verify(actions).validateMaxTimeMinutesToStateAmdSaveNewGame(chatId, state, "", msgId);
    }
}
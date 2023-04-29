package ru.coffeecoders.questbot.actions.newgame;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.actions.newgame.utils.NewGameRequests;
import ru.coffeecoders.questbot.actions.newgame.utils.NewGameUtils;
import ru.coffeecoders.questbot.entities.NewGameCreatingState;
import ru.coffeecoders.questbot.managers.BlockingManager;
import ru.coffeecoders.questbot.managers.RestrictingManager;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.services.NewGameCreatingStateService;
import ru.coffeecoders.questbot.validators.GameValidator;
import ru.coffeecoders.questbot.validators.QuestionsValidator;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewGameActionsTest {
    @Mock
    private NewGameCreatingStateService newGameCreatingStateService;
    @Mock
    private QuestionsValidator questionsValidator;
    @Mock
    private GameValidator gameValidator;
    @Mock
    private NewGameRequests requests;
    @Mock
    private NewGameUtils utils;
    @Mock
    private BlockingManager blockingManager;
    @Mock
    private RestrictingManager restrictingManager;
    @Mock
    private MessageSender msgSender;
    @Mock
    private Messages messages;
    @Mock
    private NewGameCreatingState state;

    private final long chatId = 1L;
    private final int msgId = 11;
    private final String text = "text";
    private final String gameName = "gameName";
    private final String msg = "msg";
    private final int requestMsgId = 123;
    private final int[] groupIds = new int[]{1, 2};

    @InjectMocks
    private NewGameActions actions;

    @BeforeEach
    void setUp() {
        lenient().when(state.getRequestMsgId()).thenReturn(requestMsgId);
        lenient().when(messages.invalidNumber()).thenReturn(msg);
    }

    @Test
    void createNewGameCreatingStateTest() {
        actions.createNewGameCreatingState(chatId);
        verify(newGameCreatingStateService).save(new NewGameCreatingState(chatId));
        verify(requests).requestNewGameName(chatId);
    }

    @Test
    void validateGameNameToStateAndRequestNextPartIfGameNameAlreadyTakenTest() {
        when(gameValidator.isGameNameAlreadyTaken(gameName)).thenReturn(false);
        actions.validateGameNameToStateAndRequestNextPart(chatId, state, gameName, msgId);
        verify(msgSender).sendDelete(chatId, msgId);
        verify(utils).calcRequestMsgId(state, msgId);
        verify(state).setGameName(gameName);
        verify(newGameCreatingStateService).save(state);
        verify(requests).requestQuestionGroups(gameName, chatId, requestMsgId);
    }

    @Test
    void validateGameNameToStateAndRequestNextPartIfGameNameNotTakenTest() {
        when(messages.nameAlreadyTaken()).thenReturn(text);
        when(gameValidator.isGameNameAlreadyTaken(gameName)).thenReturn(true);
        actions.validateGameNameToStateAndRequestNextPart(chatId, state, gameName, msgId);
        verify(msgSender).sendDelete(chatId, msgId);
        verify(utils).calcRequestMsgId(state, msgId);
        verify(state).setGameName(gameName);
        verify(utils).switchMsg(1, chatId, requestMsgId, state, text, null);
    }

    @Test
    void addSelectedQuestionGroupAndRefreshMsgTest() {
        int questionGroupId = 14;
        InlineKeyboardMarkup kb = new InlineKeyboardMarkup();
        when(utils.addQuestionGroupAndGetKeyboard(chatId, questionGroupId)).thenReturn(kb);
        when(messages.addedQuestionGroup()).thenReturn(text);
        when(utils.getNewGameCreatingState(chatId)).thenReturn(state);
        actions.addSelectedQuestionGroupAndRefreshMsg(chatId, msgId, questionGroupId);
        verify(utils).switchMsg(2, chatId, msgId, state, text, kb);
    }

    @Test
    void stopSelectingQuestionsGroupsAndRequestNextPartTest() {
        when(utils.getNewGameCreatingState(chatId)).thenReturn(state);
        when(state.getGroupsIds()).thenReturn(groupIds);
        actions.stopSelectingQuestionsGroupsAndRequestNextPart(chatId, msgId);
        verify(requests).requestMaxQuestionsCount(chatId, msgId);
    }

    @Test
    void validateMaxQuestionsCountToStateAndRequestNextPartValidValueTest() {
        Integer maxQuestionCount = 12;
        when(utils.parseTextToInteger(anyString())).thenReturn(maxQuestionCount);
        when(state.getGroupsIds()).thenReturn(groupIds);
        when(questionsValidator.isRequestedQuestionCountNotMoreThanWeHaveByGroups(
                maxQuestionCount, groupIds)).thenReturn(true);
        actions.validateMaxQuestionsCountToStateAndRequestNextPart(chatId, state, text, msgId);
        verify(msgSender).sendDelete(chatId, msgId);
        verify(state).setMaxQuestionsCount(maxQuestionCount);
        verify(newGameCreatingStateService).save(state);
        verify(requests).requestStartCountTasks(chatId, requestMsgId, state);
    }

    @Test
    void validateMaxQuestionsCountToStateAndRequestNextPartInvalidValueTest() {
        Integer maxQuestionCount = 12;
        when(utils.parseTextToInteger(anyString())).thenReturn(maxQuestionCount);
        when(state.getGroupsIds()).thenReturn(groupIds);
        when(messages.invalidQuestionCount()).thenReturn(msg);
        when(questionsValidator.isRequestedQuestionCountNotMoreThanWeHaveByGroups(
                maxQuestionCount, groupIds)).thenReturn(false);
        actions.validateMaxQuestionsCountToStateAndRequestNextPart(chatId, state, text, msgId);
        verify(msgSender).sendDelete(chatId, msgId);
        verify(utils).switchMsg(3, chatId, requestMsgId, state, msg, null);
    }

    @Test
    void validateMaxQuestionsCountToStateAndRequestNextPartNegativeValueTest() {
        Integer maxQuestionCount = -12;
        when(utils.parseTextToInteger(anyString())).thenReturn(maxQuestionCount);
        actions.validateMaxQuestionsCountToStateAndRequestNextPart(chatId, state, text, msgId);
        verify(msgSender).sendDelete(chatId, msgId);
        verify(utils).switchMsg(3, chatId, requestMsgId, state, msg, null);
    }

    @Test
    void validateStartCountTaskToStateAndRequestNextPartValidValueTest() {
        Integer startCountTask = 10;
        int maxQuestionCount = 30;
        when(utils.parseTextToInteger(anyString())).thenReturn(startCountTask);
        when(state.getMaxQuestionsCount()).thenReturn(maxQuestionCount);
        actions.validateStartCountTaskToStateAndRequestNextPart(chatId, state, text, msgId);
        verify(msgSender).sendDelete(chatId, msgId);
        verify(state).setStartCountTasks(startCountTask);
        verify(newGameCreatingStateService).save(state);
        verify(requests).requestMaxPerformedQuestionCount(chatId, requestMsgId, state);
    }

    @Test
    void validateStartCountTaskToStateAndRequestNextPartTooBigValueTest() {
        Integer startCountTask = 10;
        int maxQuestionCount = 3;
        when(utils.parseTextToInteger(anyString())).thenReturn(startCountTask);
        when(state.getMaxQuestionsCount()).thenReturn(maxQuestionCount);
        when(messages.startQMoreMaxQ()).thenReturn(msg);
        actions.validateStartCountTaskToStateAndRequestNextPart(chatId, state, text, msgId);
        verify(msgSender).sendDelete(chatId, msgId);
        verify(utils).switchMsg(4, chatId, requestMsgId, state, msg, null);
    }

    @Test
    void validateStartCountTaskToStateAndRequestNextPartNegativeValueTest() {
        Integer startCountTask = -10;
        when(utils.parseTextToInteger(anyString())).thenReturn(startCountTask);
        actions.validateStartCountTaskToStateAndRequestNextPart(chatId, state, text, msgId);
        verify(msgSender).sendDelete(chatId, msgId);
        verify(utils).switchMsg(4, chatId, requestMsgId, state, msg, null);
    }

    @Test
    void validateMaxPerformedQuestionsCountToStateAndRequestNextPartValidValueTest() {
        Integer maxPerformedQuestionsCount = 10;
        int maxQuestionCount = 13;
        when(utils.parseTextToInteger(anyString())).thenReturn(maxPerformedQuestionsCount);
        when(state.getMaxQuestionsCount()).thenReturn(maxQuestionCount);
        actions.validateMaxPerformedQuestionsCountToStateAndRequestNextPart(chatId, state, text, msgId);
        verify(msgSender).sendDelete(chatId, msgId);
        verify(state).setMaxPerformedQuestionsCount(maxPerformedQuestionsCount);
        verify(newGameCreatingStateService).save(state);
        verify(requests).requestMinQuestionsCountInGame(chatId, requestMsgId, state);
    }

    @Test
    void validateMaxPerformedQuestionsCountToStateAndRequestNextPartTooBigValueTest() {
        Integer maxPerformedQuestionsCount = 10;
        int maxQuestionCount = 3;
        when(utils.parseTextToInteger(anyString())).thenReturn(maxPerformedQuestionsCount);
        when(state.getMaxQuestionsCount()).thenReturn(maxQuestionCount);
        when(messages.maxPerformedQMoreMaxQ()).thenReturn(msg);
        actions.validateMaxPerformedQuestionsCountToStateAndRequestNextPart(chatId, state, text, msgId);
        verify(msgSender).sendDelete(chatId, msgId);
        verify(utils).switchMsg(5, chatId, requestMsgId, state, msg, null);
    }

    @Test
    void validateMaxPerformedQuestionsCountToStateAndRequestNextPartNegativeTest() {
        Integer maxPerformedQuestionsCount = -10;
        when(utils.parseTextToInteger(anyString())).thenReturn(maxPerformedQuestionsCount);
        when(messages.invalidNumber()).thenReturn(msg);
        actions.validateMaxPerformedQuestionsCountToStateAndRequestNextPart(chatId, state, text, msgId);
        verify(msgSender).sendDelete(chatId, msgId);
        verify(utils).switchMsg(5, chatId, requestMsgId, state, msg, null);
    }

    @Test
    void validateMinQuestionsCountInGameAndRequestNextPartValidValueTest() {
        Integer minQuestionsInGame = 4;
        when(utils.parseTextToInteger(anyString())).thenReturn(minQuestionsInGame);
        actions.validateMinQuestionsCountInGameAndRequestNextPart(chatId, state, text, msgId);
        verify(msgSender).sendDelete(chatId, msgId);
        verify(state).setMinQuestionsCountInGame(minQuestionsInGame);
        verify(newGameCreatingStateService).save(state);
        verify(requests).requestQuestionsCountToAdd(chatId, requestMsgId, state);
    }

    @Test
    void validateMinQuestionsCountInGameAndRequestNextPartInvalidValueTest() {
        Integer minQuestionsInGame = -4;
        when(utils.parseTextToInteger(anyString())).thenReturn(minQuestionsInGame);
        when(messages.invalidNumber()).thenReturn(msg);
        actions.validateMinQuestionsCountInGameAndRequestNextPart(chatId, state, text, msgId);
        verify(msgSender).sendDelete(chatId, msgId);
        verify(utils).switchMsg(6, chatId, requestMsgId, state, msg, null);
    }

    @Test
    void validateQuestionsCountToAddAndRequestNextPartValidValueTest() {
        Integer questionsToAdd = 6;
        when(utils.parseTextToInteger(anyString())).thenReturn(questionsToAdd);
        actions.validateQuestionsCountToAddAndRequestNextPart(chatId, state, text, msgId);
        verify(msgSender).sendDelete(chatId, msgId);
        verify(state).setQuestionsCountToAdd(questionsToAdd);
        verify(newGameCreatingStateService).save(state);
        verify(requests).requestMaxTimeMinutes(chatId, requestMsgId, state);
    }

    @Test
    void validateQuestionsCountToAddAndRequestNextPartInvalidValueTest() {
        Integer questionsToAdd = -6;
        when(utils.parseTextToInteger(anyString())).thenReturn(questionsToAdd);
        actions.validateQuestionsCountToAddAndRequestNextPart(chatId, state, text, msgId);
        verify(msgSender).sendDelete(chatId, msgId);
        verify(utils).switchMsg(7, chatId, requestMsgId, state, msg, null);
    }

    @Test
    void validateMaxTimeMinutesToStateAmdSaveNewGameValidValueTest() {
        Integer minutes = 90;
        String msg1 = "msg1";
        when(utils.parseTextToInteger(text)).thenReturn(minutes);
        when(messages.gameAdded()).thenReturn(msg);
        when(messages.endGameCreating()).thenReturn(msg1);
        actions.validateMaxTimeMinutesToStateAmdSaveNewGame(chatId, state, text, msgId);
        verify(msgSender).sendDelete(chatId, msgId);
        verify(state).setMaxTimeMinutes(minutes);
        verify(utils).saveNewGame(state);
        verify(newGameCreatingStateService).delete(state);
        verify(blockingManager).unblockAdminChat(chatId, msg1);
        verify(restrictingManager).unRestrictMembers(chatId);
        verify(utils).switchMsg(8, chatId, requestMsgId, state, msg, null);
    }
    @Test
    void validateMaxTimeMinutesToStateAmdSaveNewGameInvalidValueTest() {
        Integer minutes = -90;
        when(utils.parseTextToInteger(text)).thenReturn(minutes);
        actions.validateMaxTimeMinutesToStateAmdSaveNewGame(chatId, state, text, msgId);
        verify(msgSender).sendDelete(chatId, msgId);
        verify(utils).switchMsg(9, chatId, requestMsgId, state, msg, null);
    }
}
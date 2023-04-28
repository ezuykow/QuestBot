package ru.coffeecoders.questbot.actions.newgame.utils;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.NewGameCreatingState;
import ru.coffeecoders.questbot.entities.QuestionGroup;
import ru.coffeecoders.questbot.keyboards.QuestionsGroupsKeyboard;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.services.QuestionGroupService;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewGameRequestsTest {
    @Mock
    private QuestionGroupService questionGroupService;
    @Mock
    private NewGameUtils utils;
    @Mock
    private MessageSender msgSender;
    @Mock
    private Messages messages;
    @Mock
    private NewGameCreatingState state;

    private final long chatId = 1L;
    private final String msg = "message";
    private final int msgIdToEdit = 11;
    private final String text = "text";

    @InjectMocks
    private NewGameRequests requests;

    @Test
    void requestNewGameNameTest() {
        when(messages.requestNewGameName()).thenReturn(msg);
        requests.requestNewGameName(chatId);
        verify(msgSender).send(chatId, msg);
    }

    @Test
    void requestQuestionGroups() {
        String gameName = "name";
        int requestMsgId = 12;
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        when(messages.requestQuestionsGroups()).thenReturn(msg);
        when(questionGroupService.findAll()).thenReturn(List.of(new QuestionGroup(), new QuestionGroup()));
        try (MockedStatic<QuestionsGroupsKeyboard> theMock = mockStatic(QuestionsGroupsKeyboard.class)) {
            theMock.when(() -> QuestionsGroupsKeyboard.createKeyboard(anyList())).thenReturn(keyboard);
            requests.requestQuestionGroups(gameName, chatId, requestMsgId);
            verify(msgSender).edit(chatId, requestMsgId, msg, keyboard);
        }
    }

    @Test
    void requestMaxQuestionsCount() {
        when(utils.getNewGameCreatingState(chatId)).thenReturn(state);
        when(messages.requestMaxQuestionsCount()).thenReturn(msg);
        when(utils.createTextFromStateFields(msg, 2, state)).thenReturn(text);
        requests.requestMaxQuestionsCount(chatId, msgIdToEdit);
        verify(msgSender).edit(chatId, msgIdToEdit, text);
    }

    @Test
    void requestStartCountTasks() {
        when(messages.requestStartCountTasks()).thenReturn(msg);
        when(utils.createTextFromStateFields(msg, 3, state)).thenReturn(text);
        requests.requestStartCountTasks(chatId, msgIdToEdit, state);
        verify(msgSender).edit(chatId, msgIdToEdit, text);
    }

    @Test
    void requestMaxPerformedQuestionCount() {
        when(messages.requestMaxPerformedQuestionCount()).thenReturn(msg);
        when(utils.createTextFromStateFields(msg, 4, state)).thenReturn(text);
        requests.requestMaxPerformedQuestionCount(chatId, msgIdToEdit, state);
        verify(msgSender).edit(chatId, msgIdToEdit, text);
    }

    @Test
    void requestMinQuestionsCountInGame() {
        when(messages.requestMinQuestionsCountInGame()).thenReturn(msg);
        when(utils.createTextFromStateFields(msg, 5, state)).thenReturn(text);
        requests.requestMinQuestionsCountInGame(chatId, msgIdToEdit, state);
        verify(msgSender).edit(chatId, msgIdToEdit, text);
    }

    @Test
    void requestQuestionsCountToAdd() {
        when(messages.requestQuestionsCountToAdd()).thenReturn(msg);
        when(utils.createTextFromStateFields(msg, 6, state)).thenReturn(text);
        requests.requestQuestionsCountToAdd(chatId, msgIdToEdit, state);
        verify(msgSender).edit(chatId, msgIdToEdit, text);
    }

    @Test
    void requestMaxTimeMinutes() {
        when(messages.requestMaxTimeMinutes()).thenReturn(msg);
        when(utils.createTextFromStateFields(msg, 7, state)).thenReturn(text);
        requests.requestMaxTimeMinutes(chatId, msgIdToEdit, state);
        verify(msgSender).edit(chatId, msgIdToEdit, text);
    }
}
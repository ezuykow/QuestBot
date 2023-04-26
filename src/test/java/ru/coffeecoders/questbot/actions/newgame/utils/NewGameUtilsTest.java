package ru.coffeecoders.questbot.actions.newgame.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.NewGameCreatingState;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.entities.QuestionGroup;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.services.GameService;
import ru.coffeecoders.questbot.services.NewGameCreatingStateService;
import ru.coffeecoders.questbot.services.QuestionGroupService;
import ru.coffeecoders.questbot.services.QuestionService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewGameUtilsTest {
    @Mock
    private NewGameCreatingStateService newGameCreatingStateService;
    @Mock
    private QuestionGroupService questionGroupService;
    @Mock
    private QuestionService questionService;
    @Mock
    private GameService gameService;
    @Mock
    private MessageSender msgSender;
    @Mock
    private Messages messages;
    @Mock
    private NewGameCreatingState state;

    private final long chatId = 1L;
    private final int[] allGroupsIds = new int[]{1, 2};

    @InjectMocks
    private NewGameUtils utils;

    @Test
    void getNewGameCreatingStateTest() {
        when(newGameCreatingStateService.findById(chatId)).thenReturn(Optional.of(state));
        assertEquals(state, utils.getNewGameCreatingState(chatId));
    }

    @Test
    void getGroupsNamesTest() {
        String groupName1 = "groupName1";
        String groupName2 = "groupName2";
        Question q1 = new Question();
        Question q2 = new Question();
        Question q3 = new Question();
        Question q4 = new Question();
        QuestionGroup group1 = new QuestionGroup(groupName1);
        group1.setGroupId(1);
        QuestionGroup group2 = new QuestionGroup(groupName2);
        group2.setGroupId(2);
        when(questionGroupService.findAll()).thenReturn(List.of(group1, group2));
        when(questionService.findByGroupName(groupName1)).thenReturn(List.of(q1, q2));
        when(questionService.findByGroupName(groupName2)).thenReturn(List.of(q3, q4));
        String exc = "    ➕ " + groupName1 + " (вопросов: " + 2 + "),\n" + "    ➕ " + groupName2
                + " (вопросов: " + 2 + "),\n" + "    \uD83D\uDFF0 Всего вопросов со всех групп: " + 4;
        assertEquals(exc, utils.getGroupsNames(allGroupsIds));
    }

    @Test
    void addQuestionGroupIdToStateTest() {
        int questionGroupId = 3;
        int[] newGroupsIds = (ArrayUtils.add(allGroupsIds, questionGroupId));
        when(state.getGroupsIds()).thenReturn(allGroupsIds);
        when(newGameCreatingStateService.findById(chatId)).thenReturn(Optional.of(state));
        assertArrayEquals(newGroupsIds, utils.addQuestionGroupIdToState(chatId, questionGroupId));
        verify(state).setGroupsIds(newGroupsIds);
        verify(newGameCreatingStateService).save(state);
    }

    @Test
    void parseTextToIntegerValidTest() {
        String text = "12";
        assertEquals(12, utils.parseTextToInteger(text));
    }

    @Test
    void parseTextToIntegerInvalidTest() {
        String text = "ghj";
        assertNull(utils.parseTextToInteger(text));
    }

    @Test
    void calcRequestMsgIdTest() {
        int answerMsgId = 2;
        when(state.getRequestMsgId()).thenReturn(null);
        utils.calcRequestMsgId(state, answerMsgId);
        verify(state).setRequestMsgId(1);
        verify(newGameCreatingStateService).save(state);
    }

    @Test
    void addQuestionGroupAndGetKeyboardTest() {
    }

    @Test
    void switchMsg() {
    }

    @Test
    void createTextFromStateFields() {
    }

    @Test
    void saveNewGame() {
    }
}
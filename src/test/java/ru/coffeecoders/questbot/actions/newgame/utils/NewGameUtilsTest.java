package ru.coffeecoders.questbot.actions.newgame.utils;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.Game;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewGameUtilsTest {
    @Mock
    private NewGameCreatingStateService newGameCreatingStateService;
    @Mock
    private QuestionGroupService questionGroupService;
    @Mock
    private GameService gameService;
    @Mock
    private QuestionService questionService;
    @Mock
    private MessageSender msgSender;
    @Mock
    private Messages messages;
    @Mock
    private NewGameCreatingState state;

    private final long chatId = 1L;
    private final int[] allGroupsIds = new int[]{1, 2};
    private final int msgId = 44;
    private final String gameName = "gameName";
    private final String groupName = "grName";
    private final int maxQuestionCount = 30;
    private final int startCountTasks = 10;
    private final int maxPerformedQuestionsCount = 12;
    private final int minQuestionsCount = 4;
    private final int questionsCountToAdd = 6;
    private final int maxTime = 90;
    private final List<Question> list1 = List.of(new Question(), new Question());
    private final List<Question> list2 = List.of(new Question(), new Question());

    @InjectMocks
    private NewGameUtils utils;

    @BeforeEach
    void setUp() {
        lenient().when(newGameCreatingStateService.findById(chatId)).thenReturn(Optional.of(state));
        lenient().when(questionService.findByGroupName(groupName + allGroupsIds[0])).thenReturn(list1);
        lenient().when(questionService.findByGroupName(groupName + allGroupsIds[1])).thenReturn(list2);
        lenient().when(state.getGameName()).thenReturn(gameName);
        lenient().when(state.getGroupsIds()).thenReturn(allGroupsIds);
        lenient().when(state.getMaxQuestionsCount()).thenReturn(maxQuestionCount);
        lenient().when(state.getStartCountTasks()).thenReturn(startCountTasks);
        lenient().when(state.getMaxPerformedQuestionsCount()).thenReturn(maxPerformedQuestionsCount);
        lenient().when(state.getMinQuestionsCountInGame()).thenReturn(minQuestionsCount);
        lenient().when(state.getQuestionsCountToAdd()).thenReturn(questionsCountToAdd);
        lenient().when(state.getMaxTimeMinutes()).thenReturn(maxTime);
    }

    @Test
    void getNewGameCreatingStateTest() {
        assertEquals(state, utils.getNewGameCreatingState(chatId));
    }

    @Test
    void getGroupsNamesTest() {
        when(questionGroupService.findAll()).thenReturn(List.of(getQuestionGroup(allGroupsIds[0]),
                getQuestionGroup(allGroupsIds[1])));
        String exc = createCountQuestionByGroupsString();
        assertEquals(exc, utils.getGroupsNames(allGroupsIds));
    }

    @Test
    void addQuestionGroupIdToStateTest() {
        int questionGroupId = 3;
        int[] newGroupsIds = (ArrayUtils.add(allGroupsIds, questionGroupId));
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
        when(state.getRequestMsgId()).thenReturn(null);
        utils.calcRequestMsgId(state, msgId);
        verify(state).setRequestMsgId(msgId - 1);
        verify(newGameCreatingStateService).save(state);
    }

    @Test
    void addQuestionGroupAndGetKeyboardTest() {
        int newAddedQuestionGroupId = 3;
        int noAddedQuestionGroupId = 4;
        when(questionGroupService.findAll()).thenReturn(List.of(getQuestionGroup(allGroupsIds[0]),
                getQuestionGroup(allGroupsIds[1]), getQuestionGroup(newAddedQuestionGroupId),
                getQuestionGroup(noAddedQuestionGroupId)));
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.addRow(new InlineKeyboardButton(groupName + noAddedQuestionGroupId)
                        .callbackData("QuestionGroupSelected." + noAddedQuestionGroupId))
                .addRow(new InlineKeyboardButton(Character.toString(0x1F6D1) + "Закончить добавление")
                        .callbackData("QuestionGroupSelected.Stop"));
        assertEquals(keyboard, utils.addQuestionGroupAndGetKeyboard(chatId, newAddedQuestionGroupId));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9})
    void switchMsgTest(int msgN) {
        String prop1 = "prop";
        InlineKeyboardMarkup kb = new InlineKeyboardMarkup();
        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();

        lenient().when(messages.requestNewGameName()).thenReturn("🟡Введите название новой игры");
        lenient().when(messages.requestMaxQuestionsCount()).thenReturn(
                sb1.append("🟢Название игры - \"%s\"" + "\n🟢Добавленные группы: \n%s") +
                        "\n🟡Введите максимальное количество вопросов на игру");
        lenient().when(messages.requestStartCountTasks()).thenReturn(
                sb1.append("\n🟢Максимальное количество вопросов: %d") +
                        "\n🟡Введите количество вопросов, которые будут сразу доступны игрокам при старте игры");
        lenient().when(messages.requestMaxPerformedQuestionCount()).thenReturn(
                sb1.append("\n🟢Стартовое количество вопросов: %d") +
                        "\n🟡Введите количество вопросов, при ответе на которое команда становится победителем досрочно");
        lenient().when(messages.requestMinQuestionsCountInGame()).thenReturn(
                sb1.append("\n🟢Количество вопросов для победы: %d") +
                        "\n🟡Введите количество вопросов, при котором к активным вопросом добавляются новые");
        lenient().when(messages.requestQuestionsCountToAdd()).thenReturn(
                sb1.append("\n🟢Порог активных вопросов: %d") +
                        "\n🟡Введите количество вопросов, которое нужно добавлять при достижении порога");
        lenient().when(messages.requestMaxTimeMinutes()).thenReturn(
                sb1.append("\n🟢Количество добавляемых вопросов: %d") +
                        "\n🟡Введите время проведения игры в минутах");
        lenient().when(questionGroupService.findAll()).thenReturn(List.of(getQuestionGroup(allGroupsIds[0]),
                getQuestionGroup(allGroupsIds[1])));


        utils.switchMsg(msgN, chatId, msgId, state, prop1, kb);
        switch (msgN) {
            case 1 -> verify(msgSender).edit(chatId, msgId, prop1 + "🟡Введите название новой игры");
            case 2 -> verify(msgSender).edit(chatId, msgId, prop1, kb);
            case 3 -> verify(msgSender).edit(chatId, msgId, sb.append(prop1).append("🟢Название игры - \"")
                    .append(gameName).append("\"").append("\n🟢Добавленные группы: \n")
                    .append(createCountQuestionByGroupsString()) +
                    "\n🟡Введите максимальное количество вопросов на игру");
            case 4 -> msgSender.edit(chatId, msgId,
                    sb.append("\n🟢Максимальное количество вопросов: ").append(maxQuestionCount) +
                            "\n🟡Введите количество вопросов, которые будут сразу доступны игрокам при старте игры");
            case 5 -> msgSender.edit(chatId, msgId, sb.append("\n🟢Стартовое количество вопросов:")
                    .append(startCountTasks) +
                    "\n🟡Введите количество вопросов, при ответе на которое команда становится победителем досрочно");
            case 6 -> msgSender.edit(chatId, msgId, sb.append("\n🟢Количество вопросов для победы: ")
                    .append(maxPerformedQuestionsCount) +
                    "\n🟡Введите количество вопросов, при котором к активным вопросом добавляются новые");
            case 7 -> msgSender.edit(chatId, msgId, sb.append("\n🟢Порог активных вопросов: %d")
                    .append(minQuestionsCount) +
                    "\n🟡Введите количество вопросов, которое нужно добавлять при достижении порога");
            case 8 -> msgSender.edit(chatId, msgId, prop1);
            case 9 -> msgSender.edit(chatId, msgId, sb.append("\n🟢Количество добавляемых вопросов: %d")
                    .append(questionsCountToAdd) + "\n🟡Введите время проведения игры в минутах");
        }
    }

    @Test
    void saveNewGameTest() {
        utils.saveNewGame(state);
        verify(gameService).save(new Game(gameName, allGroupsIds, maxTime, maxQuestionCount,
                maxPerformedQuestionsCount, minQuestionsCount, questionsCountToAdd, startCountTasks));
    }

    private QuestionGroup getQuestionGroup(int groupId) {
        QuestionGroup qg = new QuestionGroup(groupName + groupId);
        qg.setGroupId(groupId);
        return qg;
    }

    private String createCountQuestionByGroupsString() {
        int questionGroupSize1 = list1.size();
        int questionGroupSize2 = list1.size();
        return "    ➕ " + groupName + allGroupsIds[0] + " (вопросов: " + questionGroupSize1 + "),\n" +
                "    ➕ " + groupName + allGroupsIds[1] + " (вопросов: " + questionGroupSize2 + "),\n" +
                "    \uD83D\uDFF0 Всего вопросов со всех групп: " + (questionGroupSize1 + questionGroupSize1);
    }
}
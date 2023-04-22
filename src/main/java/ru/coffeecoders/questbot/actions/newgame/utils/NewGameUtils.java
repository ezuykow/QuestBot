package ru.coffeecoders.questbot.actions.newgame.utils;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.entities.NewGameCreatingState;
import ru.coffeecoders.questbot.entities.QuestionGroup;
import ru.coffeecoders.questbot.keyboards.QuestionsGroupsKeyboard;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.services.GameService;
import ru.coffeecoders.questbot.services.NewGameCreatingStateService;
import ru.coffeecoders.questbot.services.QuestionGroupService;

import java.util.List;

/**
 * @author ezuykow
 */
@Component
public class NewGameUtils {

    private final NewGameCreatingStateService newGameCreatingStateService;
    private final QuestionGroupService questionGroupService;
    private final GameService gameService;
    private final MessageSender msgSender;
    private final Environment env;

    public NewGameUtils(NewGameCreatingStateService newGameCreatingStateService,
                        QuestionGroupService questionGroupService, GameService gameService,
                        MessageSender msgSender, Environment env)
    {
        this.newGameCreatingStateService = newGameCreatingStateService;
        this.questionGroupService = questionGroupService;
        this.gameService = gameService;
        this.msgSender = msgSender;
        this.env = env;
    }

    public NewGameCreatingState getNewGameCreatingState(long chatId) {
        return newGameCreatingStateService.findById(chatId)
                .orElseThrow(() ->
                        new RuntimeException("Этого, конечно, никогда не будет, нооо... пиздец, короче"));
    }

    public String getGroupsNames(int[] allStateGroupsIds) {
        StringBuilder sb = new StringBuilder();
        List<QuestionGroup> groups = questionGroupService.findAll();
        for (int i = 0; i < allStateGroupsIds.length; i++) {
            final int id = allStateGroupsIds[i];
            groups.stream().filter(g -> g.getGroupId() == id).findAny()
                    .ifPresent(g -> sb.append(g.getGroupName()));
            if (i < allStateGroupsIds.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public int[] addQuestionGroupIdToState(long chatId, int questionGroupId) {
        NewGameCreatingState state = getNewGameCreatingState(chatId);
        int[] groupsIds = ArrayUtils.add(state.getGroupsIds(), questionGroupId);
        state.setGroupsIds(groupsIds);
        newGameCreatingStateService.save(state);
        return groupsIds;
    }

    public Integer parseTextToInteger(String text) {
        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void calcRequestMsgId(NewGameCreatingState state, int answerMsgId) {
        if (state.getRequestMsgId() == null) {
            state.setRequestMsgId(answerMsgId - 1);
            newGameCreatingStateService.save(state);
        }
    }

    public InlineKeyboardMarkup addQuestionGroupAndGetKeyboard(long chatId, int questionGroupId) {
        int[] allStateGroupsIds = addQuestionGroupIdToState(chatId, questionGroupId);
        return QuestionsGroupsKeyboard.createKeyboard(questionGroupService.findAll()
                .stream()
                .filter(g -> !ArrayUtils.contains(allStateGroupsIds, g.getGroupId()))
                .toList());
    }

    public void switchMsg(int msgN, long chatId, int msgId, NewGameCreatingState state, String prop1, InlineKeyboardMarkup kb) {
        switch (msgN) {
            case 1 -> msgSender.edit(chatId, msgId, createTextFromStateFields(
                    prop1 + env.getProperty("messages.game.requestNewGameName"), 1, state));
            case 2 -> msgSender.edit(chatId, msgId, createTextFromStateFields(prop1, 2, state), kb);
            case 3 -> msgSender.edit(chatId, msgId, createTextFromStateFields(
                    prop1 + env.getProperty("messages.game.requestMaxQuestionsCount"), 2, state));
            case 4 -> msgSender.edit(chatId, msgId, createTextFromStateFields(
                    prop1 + env.getProperty("messages.game.requestStartCountTasks"), 3, state));
            case 5 -> msgSender.edit(chatId, msgId, createTextFromStateFields(
                    prop1 + env.getProperty("messages.game.requestMaxPerformedQuestionCount"), 4, state));
            case 6 -> msgSender.edit(chatId, msgId, createTextFromStateFields(
                    prop1 + env.getProperty("messages.game.requestMinQuestionsCountInGame"), 5, state));
            case 7 -> msgSender.edit(chatId, msgId, createTextFromStateFields(
                    prop1 + env.getProperty("messages.game.requestQuestionsCountToAdd"), 6, state));
            case 8 -> msgSender.edit(chatId, msgId, createTextFromStateFields(prop1, 7, state));
            case 9 -> msgSender.edit(chatId, msgId, createTextFromStateFields(
                    prop1 + env.getProperty("messages.game.requestMaxTimeMinutes"), 7, state));
        }
    }

    public String createTextFromStateFields(String property, int argsCount, NewGameCreatingState state) {
        Object[] args = new Object[argsCount];
        switch (argsCount) {
            case 7: args[6] = state.getQuestionsCountToAdd();
            case 6: args[5] = state.getMinQuestionsCountInGame();
            case 5: args[4] = state.getMaxPerformedQuestionsCount();
            case 4: args[3] = state.getStartCountTasks();
            case 3: args[2] = state.getMaxQuestionsCount();
            case 2: args[1] = getGroupsNames(state.getGroupsIds());
            default: args[0] = state.getGameName();
        }
        return String.format(property, args);
    }

    public void saveNewGame(NewGameCreatingState state) {
        gameService.save(
                new Game(
                        state.getGameName(),
                        state.getGroupsIds(),
                        state.getMaxTimeMinutes(),
                        state.getMaxQuestionsCount(),
                        state.getMaxPerformedQuestionsCount(),
                        state.getMinQuestionsCountInGame(),
                        state.getQuestionsCountToAdd(),
                        state.getStartCountTasks()
                )
        );
    }
}

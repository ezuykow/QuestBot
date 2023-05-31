package ru.coffeecoders.questbot.actions.newgame.utils;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.entities.NewGameCreatingState;
import ru.coffeecoders.questbot.entities.QuestionGroup;
import ru.coffeecoders.questbot.exceptions.NonExistentNewCreatingGameState;
import ru.coffeecoders.questbot.keyboards.QuestionsGroupsKeyboard;
import ru.coffeecoders.questbot.services.GameService;
import ru.coffeecoders.questbot.services.NewGameCreatingStateService;
import ru.coffeecoders.questbot.services.QuestionGroupService;
import ru.coffeecoders.questbot.services.QuestionService;

import java.util.List;

/**
 * @author ezuykow
 */
@Component
public class NewGameUtils {

    private final NewGameCreatingStateService newGameCreatingStateService;
    private final QuestionGroupService questionGroupService;
    private final QuestionService questionService;
    private final GameService gameService;

    public NewGameUtils(NewGameCreatingStateService newGameCreatingStateService,
                        QuestionGroupService questionGroupService, QuestionService questionService,
                        GameService gameService)
    {
        this.newGameCreatingStateService = newGameCreatingStateService;
        this.questionGroupService = questionGroupService;
        this.questionService = questionService;
        this.gameService = gameService;
    }

    //-----------------API START-----------------

    /**
     * @author ezuykow
     */
    public NewGameCreatingState getNewGameCreatingState(long chatId) {
        return newGameCreatingStateService.findById(chatId)
                .orElseThrow(NonExistentNewCreatingGameState::new);
    }

    /**
     * @author ezuykow
     */
    public String getGroupsNamesMsg(int[] allGroupsIds) {
        StringBuilder sb = new StringBuilder();
        int allQuestionsCount = addGroupsNamesAndGetQuestionsCount(sb, allGroupsIds);
        sb.append("    \uD83D\uDFF0 Всего вопросов со всех групп: ").append(allQuestionsCount);
        return sb.toString();
    }

    /**
     * @author ezuykow
     */
    public int[] addQuestionGroupIdToState(long chatId, int questionGroupId) {
        NewGameCreatingState state = getNewGameCreatingState(chatId);
        int[] groupsIds = ArrayUtils.add(state.getGroupsIds(), questionGroupId);
        state.setGroupsIds(groupsIds);
        newGameCreatingStateService.save(state);
        return groupsIds;
    }

    /**
     * @author ezuykow
     */
    public Integer parseTextToInteger(String text) {
        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * @author ezuykow
     */
    public void calcRequestMsgId(NewGameCreatingState state, int answerMsgId) {
        if (state.getRequestMsgId() == null) {
            state.setRequestMsgId(answerMsgId - 1);
            newGameCreatingStateService.save(state);
        }
    }

    /**
     * @author ezuykow
     */
    public InlineKeyboardMarkup addQuestionGroupAndGetKeyboard(long chatId, int questionGroupId) {
        int[] allStateGroupsIds = addQuestionGroupIdToState(chatId, questionGroupId);
        return QuestionsGroupsKeyboard.createKeyboardForPrepareGame(questionGroupService.findAll()
                .stream()
                .filter(g -> !ArrayUtils.contains(allStateGroupsIds, g.getGroupId()))
                .toList());
    }

    /**
     * @author ezuykow
     */
    public void saveNewGame(NewGameCreatingState state) {
        gameService.save(new Game(state));
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private int addGroupsNamesAndGetQuestionsCount(StringBuilder sb, int[] allStateGroupsIds) {
        int count = 0;
        if (allStateGroupsIds != null) {
            List<QuestionGroup> groups = questionGroupService.findAll();
            for (final int id : allStateGroupsIds) {
                final String groupName = groups.stream().filter(g -> g.getGroupId() == id).findAny()
                        .map(QuestionGroup::getGroupName).orElse("❌DELETED❌");
                final long questionsCount = questionService.findByGroupName(groupName).size();
                sb.append("    ➕ ").append(groupName).append(" (вопросов: ").append(questionsCount).append("),\n");
                count += questionsCount;
            }
        }
        return count;
    }
}

package ru.coffeecoders.questbot.keyboards;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import ru.coffeecoders.questbot.entities.QuestionGroup;

import java.util.List;

/**
 * @author ezuykow
 */
public class QuestionsGroupsKeyboard {

    private final InlineKeyboardMarkup keyboard;

    private QuestionsGroupsKeyboard(List<QuestionGroup> groups, boolean forViewer) {
        keyboard = new InlineKeyboardMarkup();
        createButtons(groups, forViewer);
    }

    //-----------------API START-----------------

    /**
     * Собирает клавиатуру с группами вопросов
     * @param groups группы вопросов {@link QuestionGroup}
     * @return {@link InlineKeyboardMarkup} - собранная клавиатура
     * @author ezuykow
     */
    public static InlineKeyboardMarkup createKeyboardForPrepareGame(List<QuestionGroup> groups) {
        return new QuestionsGroupsKeyboard(groups, false).keyboard;
    }

    public static InlineKeyboardMarkup createKeyboardForQuestionsViewer(List<QuestionGroup> groups) {
        return new QuestionsGroupsKeyboard(groups, true).keyboard;
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void createButtons(List<QuestionGroup> groups, boolean forViewer) {
        String dataPrefix = forViewer
                ? "QuestionViewer.QuestionGroupSelected."
                : "QuestionGroupSelected.";

        groups.forEach(g -> keyboard.addRow(
                new InlineKeyboardButton(g.getGroupName())
                        .callbackData(dataPrefix + g.getGroupId()))
        );

        if (!forViewer) {
            keyboard.addRow(
                    new InlineKeyboardButton(Character.toString(0x1F6D1) + "Закончить добавление")
                            .callbackData(dataPrefix + "Stop")
            );
        }
    }
}

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

    private QuestionsGroupsKeyboard(List<QuestionGroup> groups) {
        keyboard = new InlineKeyboardMarkup();
        createButtons(groups);
    }

    public static InlineKeyboardMarkup createKeyboard(List<QuestionGroup> groups) {
        return new QuestionsGroupsKeyboard(groups).keyboard;
    }

    private void createButtons(List<QuestionGroup> groups) {
        groups.forEach(g ->
                    keyboard.addRow(
                            new InlineKeyboardButton(g.getGroupName())
                                    .callbackData("QuestionGroupSelected." + g.getGroupId())
                    )
        );

    }
}

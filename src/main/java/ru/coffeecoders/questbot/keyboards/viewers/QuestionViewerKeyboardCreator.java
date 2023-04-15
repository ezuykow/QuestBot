package ru.coffeecoders.questbot.keyboards.viewers;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

/**
 * @author ezuykow
 */
public class QuestionViewerKeyboardCreator {

    private QuestionViewerKeyboardCreator() {}

    public static InlineKeyboardMarkup createKeyboard(
            int pageSize, boolean leftArrowNeed, int startIndex, int lastIndex, boolean rightArrowNeed) {

        InlineKeyboardButton[] buttons = new InlineKeyboardButton[pageSize + 2];
        int currentButtonIdx = 0;

        buttons[currentButtonIdx++] = leftArrowNeed
                ? new InlineKeyboardButton("\u25C0")
                .callbackData("QuestionViewer.Switch page to previous.First element index." + startIndex)
                : new InlineKeyboardButton("\u274C")
                .callbackData("QuestionViewer.Delete message");

        for (int i = startIndex; i <= lastIndex; i++) {
            buttons[currentButtonIdx++] = new InlineKeyboardButton(String.valueOf(i + 1))
                    .callbackData("QuestionViewer.Taken index." + (i + 1));
        }

        buttons[currentButtonIdx] = rightArrowNeed
                ? new InlineKeyboardButton("\u25B6")
                .callbackData("QuestionViewer.Switch page to next.Last element index." + lastIndex)
                : new InlineKeyboardButton("\u274C")
                .callbackData("QuestionViewer.Delete message");

        return new InlineKeyboardMarkup(buttons);
    }
}
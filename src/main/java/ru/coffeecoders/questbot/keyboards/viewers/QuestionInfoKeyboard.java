package ru.coffeecoders.questbot.keyboards.viewers;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import ru.coffeecoders.questbot.viewers.QuestionInfoViewer;

/**
 * @author ezuykow
 */
public class QuestionInfoKeyboard {

    private InlineKeyboardButton[] buttons;
    private final InlineKeyboardMarkup keyboard;

    private QuestionInfoKeyboard(int questionId) {
        createButtons(questionId);
        keyboard = new InlineKeyboardMarkup(buttons);
    }

    /**
     * Создает Inline-клавиатуру для {@link QuestionInfoViewer}
     * @param questionId id отображаемого вопроса
     * @return собранный InlineKeyboardMarkup
     * @see InlineKeyboardMarkup
     */
    public static InlineKeyboardMarkup createKeyboard(int questionId) {
        return new QuestionInfoKeyboard(questionId).keyboard;
    }

    private void createButtons(int questionId) {
        buttons = new InlineKeyboardButton[3];

        buttons[0] = new InlineKeyboardButton("Редактировать")
                .callbackData("QuestionViewer.QuestionInfo.Edit question." + questionId);
        buttons[1] = new InlineKeyboardButton("Удалить")
                .callbackData("QuestionViewer.QuestionInfo.Delete question." + questionId);
        buttons[2] = new InlineKeyboardButton(Character.toString(0x1F519))
                .callbackData("QuestionViewer.QuestionInfo.Back");
    }
}

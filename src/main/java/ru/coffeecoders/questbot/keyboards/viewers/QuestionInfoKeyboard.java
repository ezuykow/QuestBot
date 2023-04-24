package ru.coffeecoders.questbot.keyboards.viewers;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import ru.coffeecoders.questbot.viewers.QuestionInfoViewer;

/**
 * @author ezuykow
 */
public class QuestionInfoKeyboard {

    private final InlineKeyboardMarkup keyboard;

    private QuestionInfoKeyboard(int questionId) {
        keyboard = new InlineKeyboardMarkup(new InlineKeyboardButton("Удалить")
                .callbackData("QuestionViewer.QuestionInfo.Delete question." + questionId),
                new InlineKeyboardButton(Character.toString(0x1F519))
                        .callbackData("QuestionViewer.QuestionInfo.Back"));
    }

    //-----------------API START-----------------

    /**
     * Создает Inline-клавиатуру для {@link QuestionInfoViewer}
     * @param questionId id отображаемого вопроса
     * @return собранный {@link InlineKeyboardMarkup}
     * @author ezuykow
     */
    public static InlineKeyboardMarkup createKeyboard(int questionId) {
        return new QuestionInfoKeyboard(questionId).keyboard;
    }

    //-----------------API END-----------------
}

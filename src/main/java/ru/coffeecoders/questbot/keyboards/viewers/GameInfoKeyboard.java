package ru.coffeecoders.questbot.keyboards.viewers;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

/**
 * @author ezuykow
 */
public class GameInfoKeyboard {

    private final InlineKeyboardMarkup keyboard;

    private GameInfoKeyboard(String gameName) {
        keyboard = new InlineKeyboardMarkup(createButtons(gameName));
    }

    //-----------------API START-----------------

    public static InlineKeyboardMarkup createKeyboard(String gameName) {
        return new GameInfoKeyboard(gameName).keyboard;
    }

    //-----------------API END-----------------

    private InlineKeyboardButton[] createButtons(String gameName) {
        return new InlineKeyboardButton[]{
                new InlineKeyboardButton("Удалить")
                        .callbackData("GameViewer.Game info.Delete" + gameName),
                new InlineKeyboardButton(Character.toString(0x1F519))
                        .callbackData("GameViewer.Game info.Back")
        };
    }
}

package ru.coffeecoders.questbot.keyboards.viewers;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import ru.coffeecoders.questbot.models.GameInfoPage;

/**
 * @author ezuykow
 */
public class GameInfoKeyboard {

    private final InlineKeyboardMarkup keyboard;

    private GameInfoKeyboard(int gameId) {
        keyboard = new InlineKeyboardMarkup(createButtons(gameId));
    }

    //-----------------API START-----------------

    /**
     * Создает новый {@link GameInfoKeyboard} и возвращает его клавиатуру
     * @return {@link InlineKeyboardMarkup} - клавиатура для {@link GameInfoPage}
     * @author ezuykow
     */
    public static InlineKeyboardMarkup createKeyboard(int gameId) {
        return new GameInfoKeyboard(gameId).keyboard;
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private InlineKeyboardButton[] createButtons(int gameId) {
        return new InlineKeyboardButton[]{
                new InlineKeyboardButton("Удалить")
                        .callbackData("GameViewer.Game info.Delete." + gameId),
                new InlineKeyboardButton(Character.toString(0x1F519))
                        .callbackData("GameViewer.Game info.Back")
        };
    }
}

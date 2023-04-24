package ru.coffeecoders.questbot.keyboards.viewers;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import ru.coffeecoders.questbot.models.GameInfoPage;

/**
 * @author ezuykow
 */
public class GameInfoKeyboard {

    private final InlineKeyboardMarkup keyboard;

    private GameInfoKeyboard(String gameName) {
        keyboard = new InlineKeyboardMarkup(createButtons(gameName));
    }

    //-----------------API START-----------------

    /**
     * Создает новый {@link GameInfoKeyboard} и возвращает его клавиатуру
     * @param gameName название выбранной игры
     * @return {@link InlineKeyboardMarkup} - клавиатура для {@link GameInfoPage}
     * @author ezuykow
     */
    public static InlineKeyboardMarkup createKeyboard(String gameName) {
        return new GameInfoKeyboard(gameName).keyboard;
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private InlineKeyboardButton[] createButtons(String gameName) {
        return new InlineKeyboardButton[]{
                new InlineKeyboardButton("Удалить")
                        .callbackData("GameViewer.Game info.Delete." + gameName),
                new InlineKeyboardButton(Character.toString(0x1F519))
                        .callbackData("GameViewer.Game info.Back")
        };
    }
}

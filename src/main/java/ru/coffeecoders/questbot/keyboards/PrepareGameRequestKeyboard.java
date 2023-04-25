package ru.coffeecoders.questbot.keyboards;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.List;

/**
 * @author ezuykow
 */
public class PrepareGameRequestKeyboard {

    private final InlineKeyboardMarkup keyboard;

    private PrepareGameRequestKeyboard(List<String> gamesNames, long adminId) {
        keyboard = new InlineKeyboardMarkup();
        addButtons(gamesNames, adminId);
    }


    //-----------------API START-----------------
    /**
     * Создает экземпляр {@link PrepareGameRequestKeyboard} и возвращает его поле {@code keyboard}
     * @param gamesNames список названий игр
     * @param adminId id инициировавшего админа
     * @return {@link InlineKeyboardMarkup} - собранную клавиатуру
     * @author ezuykow
     */
    public static InlineKeyboardMarkup createKeyboard(List<String> gamesNames, long adminId) {
        return new PrepareGameRequestKeyboard(gamesNames, adminId).keyboard;
    }


    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void addButtons(List<String> gamesNames, long adminId) {
        gamesNames.forEach(g ->
                keyboard.addRow(
                        new InlineKeyboardButton("\uD83C\uDFB2 " + g)
                                .callbackData("PrepareGame." + g + "." + adminId)
                ));
        keyboard.addRow(
                new InlineKeyboardButton("❌ Отмена")
                        .callbackData("PrepareGame.Close")
        );
    }
}

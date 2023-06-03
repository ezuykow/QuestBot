package ru.coffeecoders.questbot.keyboards;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import ru.coffeecoders.questbot.entities.Game;

import java.util.List;

/**
 * @author ezuykow
 */
public class PrepareGameRequestKeyboard {

    private final InlineKeyboardMarkup keyboard;

    private PrepareGameRequestKeyboard(List<Game> games, long adminId) {
        keyboard = new InlineKeyboardMarkup();
        addButtons(games, adminId);
    }


    //-----------------API START-----------------
    /**
     * Создает экземпляр {@link PrepareGameRequestKeyboard} и возвращает его поле {@code keyboard}
     * @param adminId id инициировавшего админа
     * @return {@link InlineKeyboardMarkup} - собранную клавиатуру
     * @author ezuykow
     */
    public static InlineKeyboardMarkup createKeyboard(List<Game> games, long adminId) {
        return new PrepareGameRequestKeyboard(games, adminId).keyboard;
    }


    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void addButtons(List<Game> games, long adminId) {
        games.forEach(g ->
                keyboard.addRow(
                        new InlineKeyboardButton("\uD83C\uDFB2 " + g.getGameName())
                                .callbackData("PrepareGame." + g.getGameId() + "." + adminId)
                ));
        keyboard.addRow(
                new InlineKeyboardButton("❌ Отмена")
                        .callbackData("PrepareGame.Close." + adminId)
        );
    }
}

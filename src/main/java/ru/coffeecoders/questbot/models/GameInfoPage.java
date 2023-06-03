package ru.coffeecoders.questbot.models;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.keyboards.viewers.GameInfoKeyboard;
import ru.coffeecoders.questbot.messages.MessageBuilder;

/**
 * @author ezuykow
 */
public class GameInfoPage {

    private final String text;
    private final InlineKeyboardMarkup keyboard;

    private GameInfoPage(Game game, String prop, MessageBuilder messageBuilder) {
        text = messageBuilder.build(prop, -1, game);
        keyboard = GameInfoKeyboard.createKeyboard(game.getGameId());
    }

    //-----------------API START-----------------

    /**
     * Возвращает новый экземпляр {@link GameInfoPage}
     *
     * @param game           {@link Game} - игра, которую нужно отобразить
     * @param prop           шаблон вывода
     * @return собранный {@link GameInfoPage}
     * @author ezuykow
     */
    public static GameInfoPage createPage(Game game, String prop, MessageBuilder messageBuilder) {
        return new GameInfoPage(game, prop, messageBuilder);
    }

    /**
     * @return {@code GameInfoPage.text} - текст страницы
     * @author ezuykow
     */
    public String getText() {
        return text;
    }

    /**
     * @return {@code GameInfoPage.keyboard} - клавиатуру страницы
     * @author ezuykow
     */
    public InlineKeyboardMarkup getKeyboard() {
        return keyboard;
    }

    //-----------------API END-----------------
}

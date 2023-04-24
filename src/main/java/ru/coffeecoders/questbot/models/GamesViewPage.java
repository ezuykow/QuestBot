package ru.coffeecoders.questbot.models;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.keyboards.viewers.GamesViewerKeyboard;

import java.util.List;

/**
 * @author ezuykow
 */
public class GamesViewPage {

    private String text;
    private InlineKeyboardMarkup keyboard;

    private GamesViewPage(List<Game> games) {
        createText(games);
        createKeyboard(games);
    }

    //-----------------API START-----------------

    /**
     * Возвращает новый экземпляр {@link GamesViewPage}
     * @param games список всех игр
     * @return собранный {@link GamesViewPage}
     * @author ezuykow
     */
    public static GamesViewPage createPage(List<Game> games) {
        return new GamesViewPage(games);
    }

    /**
     * @return {@code GamesViewPage.text} - текст страницы
     * @author ezuykow
     */
    public String getText() {
        return text;
    }

    /**
     * @return {@code GamesViewPage.keyboard} - клавиатуру страницы
     * @author ezuykow
     */
    public InlineKeyboardMarkup getKeyboard() {
        return keyboard;
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void createText(List<Game> games) {
        StringBuilder sb = new StringBuilder();
        sb.append("🎲Созданные игры:\n\n");
        for (int i = 0; i < games.size(); i++) {
            sb.append(i+1).append(". ")
                    .append(games.get(i).getGameName()).append(";\n");
        }
        text = sb.toString();
    }

    /**
     * @author ezuykow
     */
    private void createKeyboard(List<Game> games) {
        keyboard = GamesViewerKeyboard.createKeyboard(games);
    }

}

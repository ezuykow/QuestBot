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

    public static GamesViewPage createPage(List<Game> games) {
        return new GamesViewPage(games);
    }

    public String getText() {
        return text;
    }

    public InlineKeyboardMarkup getKeyboard() {
        return keyboard;
    }

    private void createText(List<Game> games) {
        StringBuilder sb = new StringBuilder();
        sb.append("🎲Созданные игры:\n\n");
        for (int i = 0; i < games.size(); i++) {
            sb.append(i+1).append(". ")
                    .append(games.get(i).getGameName()).append(";\n");
        }
        text = sb.toString();
    }

    private void createKeyboard(List<Game> games) {
        keyboard = GamesViewerKeyboard.createKeyboard(games);
    }

}

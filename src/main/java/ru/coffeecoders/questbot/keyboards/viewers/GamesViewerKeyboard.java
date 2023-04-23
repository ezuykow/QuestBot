package ru.coffeecoders.questbot.keyboards.viewers;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import ru.coffeecoders.questbot.entities.Game;

import java.util.List;

/**
 * @author ezuykow
 */
public class GamesViewerKeyboard {

    private final InlineKeyboardMarkup keyboard;
    private InlineKeyboardButton[] buttons;

    private GamesViewerKeyboard(List<Game> games) {
        createButtons(games);
        keyboard = new InlineKeyboardMarkup(buttons);
    }

    public static InlineKeyboardMarkup createKeyboard(List<Game> games) {
        return new GamesViewerKeyboard(games).keyboard;
    }

    private void createButtons(List<Game> games) {
        buttons = new InlineKeyboardButton[games.size() + 1];
        for (int i = 0; i < games.size(); i++) {
            buttons[i] = new InlineKeyboardButton(String.valueOf(i + 1))
                    .callbackData("GameViewer.Taken game." + games.get(i).getGameName());
        }
        buttons[games.size()] = new InlineKeyboardButton("\u274C")
                .callbackData("GameViewer.Close");
    }
}

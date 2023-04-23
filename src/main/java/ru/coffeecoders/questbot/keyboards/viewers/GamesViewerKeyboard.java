package ru.coffeecoders.questbot.keyboards.viewers;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import ru.coffeecoders.questbot.entities.Game;

import java.util.List;

/**
 * @author ezuykow
 */
public class GamesViewerKeyboard {

    public static final int MAX_BUTTONS_COUNT_IN_ROW = 8;

    private final InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

    private GamesViewerKeyboard(List<Game> games) {
        createKb(games);
    }

    public static InlineKeyboardMarkup createKeyboard(List<Game> games) {
        return new GamesViewerKeyboard(games).keyboard;
    }

    private void createKb(List<Game> games) {
        int gamesCount = games.size();
        int rowsCount = gamesCount / MAX_BUTTONS_COUNT_IN_ROW +
                (gamesCount % MAX_BUTTONS_COUNT_IN_ROW == 0 ? 0 : 1);
        addButtonsToKeyboard(games, gamesCount, rowsCount);
    }

    private void addButtonsToKeyboard(List<Game> games, int gamesCount, int rowsCount) {
        for (int i = 0; i < rowsCount; i++) {
            keyboard.addRow(createRow(games, calcButtonsCount(i + 1, rowsCount, gamesCount),
                    i * MAX_BUTTONS_COUNT_IN_ROW));
        }
        keyboard.addRow(new InlineKeyboardButton("\u274C")
                .callbackData("GameViewer.Close"));
    }

    private InlineKeyboardButton[] createRow(List<Game> games, int buttonsCount, int startIndex) {
        InlineKeyboardButton[] buttons = new InlineKeyboardButton[buttonsCount];
        for (int i = 0; i < buttonsCount; i++, startIndex++) {
            buttons[i] = new InlineKeyboardButton(String.valueOf(startIndex + 1))
                    .callbackData("GameViewer.Taken game." + games.get(startIndex).getGameName());
        }
        return buttons;
    }

    private int calcButtonsCount(int currentRow, int rowsCount, int gamesCount) {
        return ((currentRow == rowsCount) && (gamesCount % MAX_BUTTONS_COUNT_IN_ROW != 0))
                ? gamesCount - MAX_BUTTONS_COUNT_IN_ROW * (currentRow - 1)
                : MAX_BUTTONS_COUNT_IN_ROW;
    }
}

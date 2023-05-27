package ru.coffeecoders.questbot.keyboards.viewers;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.List;

/**
 * @author ezuykow
 */
public class TeamChooserKeyboard {

    private final InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

    public TeamChooserKeyboard(List<String> teamNamesByChatId) {
        createButtons(teamNamesByChatId);
    }

    //-----------------API START-----------------

    public static InlineKeyboardMarkup createKeyboard(List<String> teamNamesByChatId) {
        return new TeamChooserKeyboard(teamNamesByChatId).keyboard;
    }

    //-----------------API END-----------------

    private void createButtons(List<String> teamNamesByChatId) {
        teamNamesByChatId.forEach(n -> keyboard.addRow(
                new InlineKeyboardButton(n)
                        .callbackData("TeamChooser." + n)
                )
        );
    }
}

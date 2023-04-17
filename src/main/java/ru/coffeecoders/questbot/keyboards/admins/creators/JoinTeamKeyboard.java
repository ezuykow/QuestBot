package ru.coffeecoders.questbot.keyboards.admins.creators;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

public class JoinTeamKeyboard {

    public static InlineKeyboardMarkup createKeyboardFromTeams(List<String[]> teams) {
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (String[] team : teams) {
            InlineKeyboardButton button = new InlineKeyboardButton(team.toString()).callbackData("team_" + team);
            buttons.add(button);
        }

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (InlineKeyboardButton button : buttons) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(button);
            rows.add(row);
        }

        return new InlineKeyboardMarkup((InlineKeyboardButton[]) rows.toArray());
    }
}
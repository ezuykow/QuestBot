package ru.coffeecoders.questbot.keyboards.admins.creators;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

public class NewTeamKeyboard {

    public static InlineKeyboardMarkup createKeyboardFromTeams(List<String> teams) {

        List<InlineKeyboardButton> buttons = new ArrayList<>();
        teams.stream().map(x -> {new InlineKeyboardButton(x),})

    }



    private InlineKeyboardButton apply(String team) {
        InlineKeyboardButton button = new InlineKeyboardButton(team);
        button.callbackData("team_" + team);
        return button;
    }
}
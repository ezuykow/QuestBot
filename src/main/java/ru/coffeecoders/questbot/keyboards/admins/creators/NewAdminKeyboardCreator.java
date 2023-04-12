package ru.coffeecoders.questbot.keyboards.admins.creators;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NewAdminKeyboardCreator {

    private List<String[]> users;
    private List<InlineKeyboardButton> buttons = new ArrayList<>();

    public InlineKeyboardButton newAdminButtonCreate(String firstName, String lastName, String username) {
        StringBuilder builderName = new StringBuilder().append(firstName).append(" ").append(lastName).append(" - ").append(username);
        InlineKeyboardButton button = new InlineKeyboardButton(builderName.toString());
        StringBuilder builderCallback = new StringBuilder().append("newadmin_").append(username);
        button.callbackData(builderCallback.toString());
        return button;
    }

    public void addButton(InlineKeyboardButton button) {
        buttons.add(button);
    }

    public InlineKeyboardMarkup createKeyboard() {
        InlineKeyboardButton[][] keyboardButtons = new InlineKeyboardButton[buttons.size()][1];
        for (int i = 0; i < buttons.size(); i++) {
            keyboardButtons[i][0] = buttons.get(i);
        }
        return new InlineKeyboardMarkup(keyboardButtons);
    }

    public InlineKeyboardMarkup newAdminKeyboardCreate() {
        for (String[] user : users) {
            String firstName = user[0];
            String lastName = user[1];
            String username = user[2];
            addButton(newAdminButtonCreate(firstName, lastName, username));
        }
        return createKeyboard();
    }
}
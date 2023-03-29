package ru.coffeecoders.questbot.commands.admins.keybords.keyboards;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;

@Component
public class AdminsCommandsManager {

    private final KeyboardFactory.KeyboardType keyboardType;
    private final KeyboardFactory keyboardFactory;


    private AdminsCommandsManager(KeyboardFactory.KeyboardType keyboardType, KeyboardFactory keyboardFactory) {
        this.keyboardType = keyboardType;
        this.keyboardFactory = keyboardFactory;
    }

    public void chooseKeyboard(KeyboardFactory.KeyboardType keyboardType, Update update) {
        if (isAdmin(update.message().chat().id()) && update.message().text().contains(keyboardType.name())) {
            KeyboardFactory.createKeyboard(update.message().text());
        } else {

        }
    }
}



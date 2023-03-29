package ru.coffeecoders.questbot.commands.admins.keybords;

import org.springframework.stereotype.Component;

@Component
public class KeyboardFactory {


    public enum KeyboardType {
        NEW_GAME
    }

    public KeyboardFactory(KeyboardSender keyboardSender) {
        this.keyboardSender = keyboardSender;
    }

    private final KeyboardSender keyboardSender;

    public void createKeyboard(KeyboardType keyboardType){
        switch (keyboardType){
            case NEW_GAME -> keyboardSender.sendKeyboard(new NewGameAdminKeyboard());
        }

    }
}



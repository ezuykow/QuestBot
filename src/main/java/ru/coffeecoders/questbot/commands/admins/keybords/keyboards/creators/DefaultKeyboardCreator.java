package ru.coffeecoders.questbot.commands.admins.keybords.keyboards.creators;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultKeyboardCreator extends ReplyKeyboardMarkup implements Keyboard {




    @Value("${keyboard.defaultKb.toGroup}")
    private static String goToGroup;
    @Value("${keyboard.defaultKb.returnKb}")
    private static String buttonReturn;

    public DefaultKeyboardCreator(KeyboardButton[]... keyboard) {
        super(keyboard);
    }



    public static ReplyKeyboardMarkup createDefaultKeyboard() {
        KeyboardButton[] buttonArray = makeButtonArray();
        ReplyKeyboardMarkup keyboardMarkup = makeKeyboard(buttonArray);
        return keyboardMarkup;
    }

    private static KeyboardButton[] makeButtonArray() {
        KeyboardButton returnKb = new KeyboardButton(buttonReturn);
        KeyboardButton toGroup = new KeyboardButton(goToGroup);
        return new KeyboardButton[] {toGroup, returnKb };
    }


    private static ReplyKeyboardMarkup makeKeyboard(KeyboardButton[] buttonRows) {
        return new ReplyKeyboardMarkup(buttonRows);
    }
}


package ru.coffeecoders.questbot.commands.admins.keybords.keyboards.creators;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NewGameKeyboardCreator extends ReplyKeyboardMarkup implements Keyboard {

    private NewGameKeyboardCreator newGameKeyboard;

    @Value("${keyboard.newGame.returnMain}")
    private static String buttonReturn;
    @Value("${keyboard.newGame.freeMode}")
    private static String buttonFreeMode;
    @Value("${keyboard.newGame.back}")
    private static String buttonBack;
    @Value("${keyboard.newGame.stationMode}")
    private static String stationMode;

    private NewGameKeyboardCreator(KeyboardButton[]... keyboard) {
        super(keyboard);
    }
    public NewGameKeyboardCreator getNewGameKeyboard() {
        return newGameKeyboard;
    }

    public static ReplyKeyboardMarkup newGameKeyboardCreate() {
        KeyboardButton[] buttonArray = makeButtonArray();
        KeyboardButton[][] buttonRows = makeRows(buttonArray);
        ReplyKeyboardMarkup keyboardMarkup = makeKeyboard(buttonRows);
        return keyboardMarkup;
        //перенести в менеджер: создать клаву - > отправить логику.

    }

    private static KeyboardButton[] makeButtonArray() {
        KeyboardButton returnToMain = new KeyboardButton(buttonReturn);
        KeyboardButton freeModeButton = new KeyboardButton(buttonFreeMode);
        KeyboardButton byStationsButton = new KeyboardButton(stationMode);
        KeyboardButton backButton = new KeyboardButton(buttonBack);
        return new KeyboardButton[] {returnToMain, freeModeButton, byStationsButton, backButton};
    }

    private static KeyboardButton[][] makeRows(KeyboardButton[] buttonArray) {
        KeyboardButton[] firstRow = new KeyboardButton[] {buttonArray[0], buttonArray[1]};
        KeyboardButton[] secondRow = new KeyboardButton[] {buttonArray[2], buttonArray[3]};
        return new KeyboardButton[][] {firstRow, secondRow};
    }

    private static ReplyKeyboardMarkup makeKeyboard(KeyboardButton[][] buttonRows) {
        return new ReplyKeyboardMarkup(buttonRows);
    }
}



package ru.coffeecoders.questbot.keyboards.admins.creators;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MainAdminsKeyboardCreator {


    @Value("${keyboard.mainKb.newGame}")
    private static String buttonNewGame;
    @Value("${keyboard.mainKb.belongGame}")
    private static String buttonCurrentGame;
    @Value("${keyboard.mainKb.questions}")
    private static String buttonQuestions;

    private MainAdminsKeyboardCreator(MainAdminsKeyboardCreator mainAdminsKeyboardCreator) {
        this.mainAdminsKeyboardCreator = mainAdminsKeyboardCreator;
    }

    MainAdminsKeyboardCreator mainAdminsKeyboardCreator;

    public MainAdminsKeyboardCreator getMainKeyboardCreator() {
        return mainAdminsKeyboardCreator;
    }

    public static Keyboard MainKeyboardCreate() {
        KeyboardButton[] buttonArray = makeButtonArray();
        KeyboardButton[][] buttonRows = makeRows(buttonArray);
        ReplyKeyboardMarkup keyboardMarkup = makeKeyboard(buttonRows);
        return keyboardMarkup;
        //перенести в менеджер: создать клаву - > отправить логику.

    }

    private static KeyboardButton[] makeButtonArray() {
        KeyboardButton returnToMain = new KeyboardButton(buttonNewGame);
        KeyboardButton freeModeButton = new KeyboardButton(buttonCurrentGame);
        KeyboardButton byStationsButton = new KeyboardButton(buttonQuestions);
        return new KeyboardButton[] {returnToMain, freeModeButton, byStationsButton};
    }


    private static KeyboardButton[][] makeRows(KeyboardButton[] buttonArray) {
        KeyboardButton[] firstRow = new KeyboardButton[] {buttonArray[0], buttonArray[1]};
        KeyboardButton[] secondRow = new KeyboardButton[] {buttonArray[2]};
        return new KeyboardButton[][] {firstRow, secondRow};
    }



    private static ReplyKeyboardMarkup makeKeyboard(KeyboardButton[][] buttonRows) {
        return new ReplyKeyboardMarkup(buttonRows);
    }

}

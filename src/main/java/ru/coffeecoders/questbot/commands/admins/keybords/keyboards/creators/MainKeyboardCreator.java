package ru.coffeecoders.questbot.commands.admins.keybords.keyboards.creators;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MainKeyboardCreator extends ReplyKeyboardMarkup implements Keyboard{


    @Value("${keyboard.mainKb.newGame}")
    private static String buttonNewGame;
    @Value("${keyboard.mainKb.belongGame}")
    private static String buttonCurrentGame;
    @Value("${keyboard.mainKb.questions}")
    private static String buttonQuestions;

    private MainKeyboardCreator(KeyboardButton[]... keyboard) {
        super(keyboard);
    }

    MainKeyboardCreator mainKeyboardCreator;

    public MainKeyboardCreator getMainKeyboardCreator() {
        return mainKeyboardCreator;
    }

    public static ReplyKeyboardMarkup MainKeyboardCreate() {
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

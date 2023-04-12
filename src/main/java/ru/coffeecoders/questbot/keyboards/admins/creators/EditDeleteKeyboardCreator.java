package ru.coffeecoders.questbot.keyboards.admins.creators;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EditDeleteKeyboardCreator {


    @Value("${keyboard.allQuestions.edit}")
    private String edits;

    @Value("${keyboard.allQuestions.delete}")
    private  String deletes;


    private EditDeleteKeyboardCreator() {
        this.editDeleteKeyboardCreator = editDeleteKeyboardCreator;
    }


    public EditDeleteKeyboardCreator getQuestionKeyboardCreator() {
        return editDeleteKeyboardCreator;
    }

    EditDeleteKeyboardCreator editDeleteKeyboardCreator;


    public static Keyboard createQuestionKeyboard() {
        EditDeleteKeyboardCreator keyboardCreator = new EditDeleteKeyboardCreator();
        KeyboardButton[] buttonArray = keyboardCreator.makeButtonArray();
        KeyboardButton[][] buttonRows = keyboardCreator.makeRows(buttonArray);
        ReplyKeyboardMarkup keyboardMarkup = keyboardCreator.makeKeyboard(buttonRows);
        return keyboardMarkup;
    }

    private KeyboardButton[] makeButtonArray() {
        KeyboardButton returnKb = new KeyboardButton(edits);
        KeyboardButton toGroup = new KeyboardButton(deletes);
        return new KeyboardButton[]{toGroup, returnKb};
    }

    private KeyboardButton[][] makeRows(KeyboardButton[] buttonArray) {
        KeyboardButton[] firstRow = new KeyboardButton[]{buttonArray[0]};
        KeyboardButton[] secondRow = new KeyboardButton[]{buttonArray[1]};
        return new KeyboardButton[][]{firstRow, secondRow};
    }

    private ReplyKeyboardMarkup makeKeyboard(KeyboardButton[][] buttonRows) {
        return new ReplyKeyboardMarkup(buttonRows);
    }
}

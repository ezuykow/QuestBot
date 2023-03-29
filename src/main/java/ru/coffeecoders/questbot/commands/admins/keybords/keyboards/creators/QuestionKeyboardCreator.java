package ru.coffeecoders.questbot.commands.admins.keybords.keyboards.creators;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class QuestionKeyboardCreator extends ReplyKeyboardMarkup implements Keyboard {


    @Value("${keyboard.questions.show}")
    private static String showQuestions;
    @Value("${keyboard.questions.add}")
    private static String addNewQuestion;

    public QuestionKeyboardCreator(KeyboardButton[]... keyboard) {
        super(keyboard);
    }

    public QuestionKeyboardCreator getQuestionKeyboardCreator() {
        return questionKeyboardCreator;
    }

    QuestionKeyboardCreator questionKeyboardCreator;


    public static ReplyKeyboardMarkup createQuestionKeyboard() {
    KeyboardButton[] buttonArray = makeButtonArray();
    KeyboardButton[][] buttonRows = makeRows(buttonArray);
    ReplyKeyboardMarkup keyboardMarkup = makeKeyboard(buttonRows);
        return keyboardMarkup;
    }

    private static KeyboardButton[] makeButtonArray() {
    KeyboardButton returnKb = new KeyboardButton(addNewQuestion);
    KeyboardButton toGroup = new KeyboardButton(showQuestions);
        return new KeyboardButton[] {toGroup, returnKb};
    }

    private static KeyboardButton[][] makeRows(KeyboardButton[] buttonArray) {
    KeyboardButton[] firstRow = new KeyboardButton[] {buttonArray[0]};
    KeyboardButton[] secondRow = new KeyboardButton[] {buttonArray[1]};
        return new KeyboardButton[][] {firstRow, secondRow};
    }

    private static ReplyKeyboardMarkup makeKeyboard(KeyboardButton[][] buttonRows) {
        return new QuestionKeyboardCreator(buttonRows);
    }
}

package ru.coffeecoders.questbot.keyboards.admins.creators;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EditDeleteKeyboardCreator {


    @Value("${keyboard.allQuestions.edit}")
    private static String edits;

    @Value("${keyboard.allQuestions.delete}")
    private static String deletes;


    private EditDeleteKeyboardCreator(EditDeleteKeyboardCreator editDeleteKeyboardCreator) {
        this.editDeleteKeyboardCreator = editDeleteKeyboardCreator;
    }


    public EditDeleteKeyboardCreator getQuestionKeyboardCreator() {
        return editDeleteKeyboardCreator;
    }

    EditDeleteKeyboardCreator editDeleteKeyboardCreator;


    public static Keyboard createQuestionKeyboard() {
        KeyboardButton[] buttonArray = makeButtonArray();
        KeyboardButton[][] buttonRows = makeRows(buttonArray);
        ReplyKeyboardMarkup keyboardMarkup = makeKeyboard(buttonRows);
        return keyboardMarkup;
    }

    private static KeyboardButton[] makeButtonArray() {
        KeyboardButton returnKb = new KeyboardButton(edits);
        KeyboardButton toGroup = new KeyboardButton(deletes);
        return new KeyboardButton[]{toGroup, returnKb};
    }

    private static KeyboardButton[][] makeRows(KeyboardButton[] buttonArray) {
        KeyboardButton[] firstRow = new KeyboardButton[]{buttonArray[0]};
        KeyboardButton[] secondRow = new KeyboardButton[]{buttonArray[1]};
        return new KeyboardButton[][]{firstRow, secondRow};
    }

    private static ReplyKeyboardMarkup makeKeyboard(KeyboardButton[][] buttonRows) {
        return new ReplyKeyboardMarkup(buttonRows);
    }
}
// добавляем сразу в базу данных
//    public static void  addQuestions() {
//    // возможно парсить по блокам через инлайн "вопрос" - "ответ" - "группа" - "показать"? введите вопрос /вопрос: Кто обитает на дне океана введите ответ /ответ: Спанч боб
//        return null;
//    }
//    public static void deleteQuestions() {
//    }
//
//    public static void editQuestions() {
//        return ;
//    }
//}

package ru.coffeecoders.questbot.keyboards.admins.creators;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Question;

import java.util.List;

@Component
public class KeyboardRESTCreator {


    @Value("${keyboard.allQuestions.edit}")
    private static String edits;

    @Value("${keyboard.allQuestions.delete}")
    private static String deletes;


    private KeyboardRESTCreator(KeyboardRESTCreator keyboardRESTCreator) {
        this.keyboardRESTCreator = keyboardRESTCreator;
    }

    public KeyboardRESTCreator getQuestionKeyboardCreator() {
        return keyboardRESTCreator;
    }

    KeyboardRESTCreator keyboardRESTCreator;




    public static Keyboard createQuestionKeyboard() {
    KeyboardButton[] buttonArray = makeButtonArray();
    KeyboardButton[][] buttonRows = makeRows(buttonArray);
    ReplyKeyboardMarkup keyboardMarkup = makeKeyboard(buttonRows);
        return keyboardMarkup;
    }

    private static KeyboardButton[] makeButtonArray() {
    KeyboardButton returnKb = new KeyboardButton(edits);
    KeyboardButton toGroup = new KeyboardButton(deletes);
        return new KeyboardButton[] {toGroup, returnKb};
    }

    private static KeyboardButton[][] makeRows(KeyboardButton[] buttonArray) {
    KeyboardButton[] firstRow = new KeyboardButton[] {buttonArray[0]};
    KeyboardButton[] secondRow = new KeyboardButton[] {buttonArray[1]};
        return new KeyboardButton[][] {firstRow, secondRow};
    }

    private static ReplyKeyboardMarkup makeKeyboard(KeyboardButton[][] buttonRows) {
        return new ReplyKeyboardMarkup(buttonRows);
    }

// добавляем сразу в базу данных
    private void addQuestions(List<Question> questions) {
        for (int i = 0; i < questions.size(); i++) {
            int questionNumber = questionMap.size() + 1;
            questionMap.put(questionNumber, questions.get(i));
        }
    }
}

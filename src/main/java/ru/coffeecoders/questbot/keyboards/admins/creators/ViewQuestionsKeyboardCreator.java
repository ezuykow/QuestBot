package ru.coffeecoders.questbot.keyboards.admins.creators;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.stereotype.Component;

@Component
public class ViewQuestionsKeyboardCreator {

    private ViewQuestionsKeyboardCreator viewQuestionsKeyboardCreator;
    private ViewQuestionsKeyboardCreator(ViewQuestionsKeyboardCreator viewQuestionsKeyboardCreator) {
        this.viewQuestionsKeyboardCreator = viewQuestionsKeyboardCreator;
    }

    public ViewQuestionsKeyboardCreator getAllQuestionsCreator() {
        return viewQuestionsKeyboardCreator;
    }



    //логика создания InlineKeyboardMarkup
    public static InlineKeyboardMarkup inlineKeyboardCreate() {
        InlineKeyboardButton[] buttonArrows = makeButtonArrows();
        InlineKeyboardButton[] buttonNumbers = createButtonNumber();

        InlineKeyboardButton[][] buttonRows = makeRows(buttonArrows);
        InlineKeyboardMarkup inlineKeyboardMarkup = makeArrowKeyboard(buttonRows);
        return inlineKeyboardMarkup;

    }

    //создание кнопки с номером вопроса
    private static InlineKeyboardButton[] createButtonNumber(int count) {
        InlineKeyboardButton[] buttons = new InlineKeyboardButton[count];
        for (int i = 0; i < count; i++) {
            buttons[i] = new InlineKeyboardButton(String.valueOf(i + 1));
        }
        return buttons;
    }




    // создание массива с кнопками влево вправо
    private static InlineKeyboardButton[] makeButtonArrows() {

        InlineKeyboardButton right = new InlineKeyboardButton("\t→");
        InlineKeyboardButton left = new InlineKeyboardButton("\t←");

            return new InlineKeyboardButton[] {left, right};
    }




    private static InlineKeyboardButton [][] makeRows(InlineKeyboardButton [] buttonArrowArray,
                                                      InlineKeyboardButton [] buttonNumbers) {
        return new InlineKeyboardButton [][] {buttonNumbers, buttonArrowArray};
    }
     private static InlineKeyboardMarkup makeArrowKeyboard(InlineKeyboardButton[][] buttonRows) {
        return new InlineKeyboardMarkup(buttonRows);
    }

}
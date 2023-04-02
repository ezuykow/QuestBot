package ru.coffeecoders.questbot.keyboards.admins.creators;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

public class ViewQuestionsUpdateCreator {
    static  boolean isFirst;
    static boolean  isLast;
    static int  firstNumber;
    static int numberOfButton;

    public static InlineKeyboardMarkup viewKeyboardCreate() {
        InlineKeyboardButton[] buttonArrows = makeButtonArrows(isFirst, isLast);
        InlineKeyboardButton[] buttonNumbers = createButtonNumber(firstNumber,numberOfButton);

        InlineKeyboardButton[][] buttonRows = makeRows(buttonNumbers,buttonArrows);
        InlineKeyboardMarkup inlineKeyboardMarkup = makeNumbersKeyboard(buttonRows);
        return inlineKeyboardMarkup;

    }

    private static InlineKeyboardButton[] makeButtonArrows(boolean isLast, boolean isFirst)  {

        InlineKeyboardButton left = null;
        InlineKeyboardButton right = null;

        if (isFirst && !isLast) {
            right = new InlineKeyboardButton("\t→");
        }
        if (!isFirst && isLast) {
            left = new InlineKeyboardButton("\t←");
        }
        if (!isFirst && !isLast) {
            left = new InlineKeyboardButton("\t←");
            right = new InlineKeyboardButton("\t→");
        }
        return new InlineKeyboardButton[] { left, right };
    }

    private static InlineKeyboardButton[] createButtonNumber(int firstNumber, int numberOfButton) {

        InlineKeyboardButton[] buttons = new InlineKeyboardButton[numberOfButton];
        for (int i = 0; i <  numberOfButton; i++) {
            buttons[i] = new InlineKeyboardButton(String.valueOf(firstNumber + i));
            buttons[i].callbackData("question_"+(firstNumber + i));
        }

        return buttons;
    }

    private static InlineKeyboardButton[][] makeRows(InlineKeyboardButton[] buttonArrowArray,
                                                     InlineKeyboardButton[] buttonNumbers) {
        return new InlineKeyboardButton[][] {buttonNumbers, buttonArrowArray};
    }

    private static InlineKeyboardMarkup makeNumbersKeyboard(InlineKeyboardButton[][] buttonRows) {
        return new InlineKeyboardMarkup(buttonRows);
    }

}

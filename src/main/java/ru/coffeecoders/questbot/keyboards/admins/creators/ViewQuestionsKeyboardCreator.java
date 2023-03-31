package ru.coffeecoders.questbot.keyboards.admins.creators;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ViewQuestionsKeyboardCreator {
    @Value("${keyboard.allQuestions.edit}")
    private static String edits;

   @Value("${keyboard.allQuestions.delete}")
    private static String deletes;
    private ViewQuestionsKeyboardCreator viewQuestionsKeyboardCreator;

    @Value("${keyboard.allQuestions.show_news}")
    static
    String show_news;

    private ViewQuestionsKeyboardCreator(ViewQuestionsKeyboardCreator viewQuestionsKeyboardCreator) {
        this.viewQuestionsKeyboardCreator = viewQuestionsKeyboardCreator;
    }

    public ViewQuestionsKeyboardCreator getAllQuestionsCreator() {
        return viewQuestionsKeyboardCreator;
    }

    public static InlineKeyboardMarkup inlineKeyboardCreate() {
        InlineKeyboardButton[] buttonArrow = makeButtonArrow();
        InlineKeyboardButton[][] buttonRows = makeRows(buttonArrow);
        InlineKeyboardMarkup inlineKeyboardMarkup = makeArrowKeyboard(buttonRows);
        return inlineKeyboardMarkup;

    }

    private static InlineKeyboardButton[] makeButtonArrow() {
        InlineKeyboardButton right = new InlineKeyboardButton("\t→");
        InlineKeyboardButton left = new InlineKeyboardButton("\t←");
        InlineKeyboardButton delete = new InlineKeyboardButton(deletes);
        InlineKeyboardButton edit = new InlineKeyboardButton(edits);
        InlineKeyboardButton showNewList = new InlineKeyboardButton(show_news);

            return new InlineKeyboardButton[] {left, right,edit,delete};
    }
    private static InlineKeyboardButton [][] makeRows(InlineKeyboardButton [] buttonArray) {
        InlineKeyboardButton [] firstRow = new InlineKeyboardButton [] {buttonArray[0], buttonArray[1]};
        InlineKeyboardButton [] secondRow = new InlineKeyboardButton [] {buttonArray[2]};
        InlineKeyboardButton [] thirdRow = new InlineKeyboardButton [] {buttonArray[3]};
        InlineKeyboardButton [] fourthRow = new InlineKeyboardButton [] {buttonArray[4]};

        return new InlineKeyboardButton [][] {firstRow, secondRow,thirdRow, fourthRow};
    }
     private static InlineKeyboardMarkup makeArrowKeyboard(InlineKeyboardButton[][] buttonRows) {
        return new InlineKeyboardMarkup(buttonRows);
    }

}
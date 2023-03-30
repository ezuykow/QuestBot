package ru.coffeecoders.questbot.commands.admins.keybords.keyboards.creators;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AllQuestionsKeyboardCreator extends InlineKeyboardMarkup implements Keyboard{
    @Value("${keyboard.allQuestions.edit}")
    private static String edits;

   @Value("${keyboard.allQuestions.toGroup}")
    private static String deletes;
    private AllQuestionsKeyboardCreator allQuestionsKeyboardCreator;


    private AllQuestionsKeyboardCreator(AllQuestionsKeyboardCreator allQuestionsKeyboardCreator) {
        this.allQuestionsKeyboardCreator = allQuestionsKeyboardCreator;
    }

    public AllQuestionsKeyboardCreator getAllQuestionsCreator() {
        return allQuestionsKeyboardCreator;
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

            return new InlineKeyboardButton[] {left, right,edit,delete};
    }
    private static InlineKeyboardButton [][] makeRows(InlineKeyboardButton [] buttonArray) {
        InlineKeyboardButton [] firstRow = new InlineKeyboardButton [] {buttonArray[0], buttonArray[1]};
        InlineKeyboardButton [] secondRow = new InlineKeyboardButton [] {buttonArray[2]};
        InlineKeyboardButton [] thirdRow = new InlineKeyboardButton [] {buttonArray[3]};

        return new InlineKeyboardButton [][] {firstRow, secondRow,thirdRow};
    }




    private static InlineKeyboardMarkup makeArrowKeyboard(InlineKeyboardButton[][] buttonRows) {
        return new InlineKeyboardMarkup(buttonRows);
    }

    //TODO нужно перененести эту логику, попробую сделать


}
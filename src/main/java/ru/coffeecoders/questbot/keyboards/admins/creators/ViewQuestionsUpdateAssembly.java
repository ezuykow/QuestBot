package ru.coffeecoders.questbot.keyboards.admins.creators;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Question;

import java.util.Map;
import java.util.TreeMap;
@Component
public class ViewQuestionsUpdateAssembly {

    private static TreeMap<Integer, Question> questionMap = new TreeMap<Integer, Question>();
    private static int pageCounter = 0;
    private static int questionsInMap = 0;

    public void refreshQuestions

    public static String getQuestionsFromIndex() {
        TreeMap<Integer, Question> result = new TreeMap<>();
        int i = 0;
        for (Map.Entry<Integer, Question> entry : questionMap.entrySet()) {
            if (i >= pageCounter && i < pageCounter + 5) {
                result.put(entry.getKey(), entry.getValue());
            }
            i++;
        }
        questionsInMap = questionMap.size();
        String resultString = result.toString();
        return resultString;
    }

    public static InlineKeyboardMarkup inlineKeyboardCreate() {
        InlineKeyboardButton[] buttonArrows = makeButtonArrows();
        InlineKeyboardButton[] buttonNumbers = createButtonNumber(pageCounter);

        InlineKeyboardButton[][] buttonRows = makeRows(buttonNumbers,buttonArrows);
        InlineKeyboardMarkup inlineKeyboardMarkup = makeArrowKeyboard(buttonRows);
        return inlineKeyboardMarkup;

    }

    private static InlineKeyboardButton[] createButtonNumber(int count) {
        InlineKeyboardButton[] buttons = new InlineKeyboardButton[count];
        for (int i = 0; i < count; i++) {
            buttons[i] = new InlineKeyboardButton(String.valueOf(pageCounter + i + 1));
        }
        return buttons;
    }

    public static InlineKeyboardMarkup pager() {
        TreeMap<Integer, Question> result = getQuestionsFromIndex(pageCounter);
        InlineKeyboardMarkup keyboard = inlineKeyboardCreate();
        pageCounter += 5;
        return keyboard;
    }

    public static InlineKeyboardMarkup lastPager() {
        pageCounter -= 5;
        return pager();
    }

    private static InlineKeyboardButton[] makeButtonArrows() {
        int questionsInMap = questionMap.size();
        boolean lastKeyInView = questionsInMap % 5 == 0 && pageCounter >= questionsInMap - 5;

        InlineKeyboardButton left = null;
        InlineKeyboardButton right = null;

        if (questionsInMap > 5 && pageCounter >= 5) {
            left = new InlineKeyboardButton("\t←");
        }
        if (questionsInMap > pageCounter + 5) {
            right = new InlineKeyboardButton("\t→");
        }
        if (lastKeyInView) {
            left = null;
        }
        if (!lastKeyInView && right == null) {
            right = new InlineKeyboardButton("\t→");
        }

        return new InlineKeyboardButton[] { left, right };
    }

    private static InlineKeyboardButton[][] makeRows(InlineKeyboardButton[] buttonArrowArray,
                                                     InlineKeyboardButton[] buttonNumbers) {
        return new InlineKeyboardButton[][] { buttonNumbers, buttonArrowArray };
    }

    private static InlineKeyboardMarkup makeArrowKeyboard(InlineKeyboardButton[][] buttonRows) {
        return new InlineKeyboardMarkup(buttonRows);
    }

}
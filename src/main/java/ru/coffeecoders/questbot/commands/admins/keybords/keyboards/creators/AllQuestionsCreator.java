package ru.coffeecoders.questbot.commands.admins.keybords.keyboards.creators;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.repositories.QuestionRepository;

import java.util.ArrayList;

import java.util.List;

@Component
public class AllQuestionsCreator extends InlineKeyboardMarkup implements Keyboard{
    @Value("${keyboard.allQuestions.edit}")
    private static String edits;

   @Value("${keyboard.allQuestions.toGroup}")
    private static String deletes;

    private List<Question> allQuestions = new ArrayList<>();
    private List<List<Question>> paginatedQuestions = new ArrayList<>();
    private int currentPage = 0;
    private AllQuestionsCreator allQuestionsCreator;

    private AllQuestionsCreator(AllQuestionsCreator allQuestionsCreator) {
        this.allQuestionsCreator = allQuestionsCreator;
    }

    public AllQuestionsCreator getAllQuestionsCreator() {
        return allQuestionsCreator;
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
    public void resizeQuestionsList() {

        paginatedQuestions = QuestionService.allQuestionsBySix(allQuestions);
    }

    public List<List<Question>>allQuestionsBySix(List<Question> allQuestion) {
        allQuestions = QuestionRepository.getAllQuestion();
        List<List<Question>> paginatedQuestions = new ArrayList<>();
        int page = 0;
        while (page * 6 < allQuestions.size()) {
            int fromIndex = page * 6;
            int toIndex = Math.min((page + 1) * 6, allQuestions.size());
            paginatedQuestions.add(allQuestions.subList(fromIndex, toIndex));
            page++;
        }

        return paginatedQuestions;
    }


}
package ru.coffeecoders.questbot.keyboards.admins.creators;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.coffeecoders.questbot.entities.Question;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ViewQuestionsKeyboardCreator {
//TODO определить где находится мапа и лист


    private static final int pageSize = 5;
    private static final Sort.Direction sortDirection = Sort.Direction.ASC;

    private static final QuestionPaginationRepository questionPaginationRepository;
    protected static final Map<Integer, Integer> userPageMap = new HashMap<>();

    public ViewQuestionsKeyboardCreator(QuestionPaginationRepository questionPaginationRepository) {
        this.questionPaginationRepository = questionPaginationRepository;
    }

    public static InlineKeyboardMarkup createQuestionsKeyboard(int messageId) {
        int page = userPageMap.getOrDefault(messageId, 0);
        PageRequest pageRequest = createPageRequest(page);
        Page<Question> questionsPage = questionPaginationRepository.findAll(pageRequest);

        List<InlineKeyboardButton> questionButtons = createQuestionButtons(questionsPage);

        List<InlineKeyboardButton> navigationButtons = createNavigationButtons(questionsPage);

        InlineKeyboardButton[][] keyboardRows = {questionButtons.toArray(new InlineKeyboardButton[0]), navigationButtons.toArray(new InlineKeyboardButton[0])};
        return new InlineKeyboardMarkup(keyboardRows);
    }

    protected static PageRequest createPageRequest(int page) {
        return PageRequest.of(page, pageSize, sortDirection, "id");
    }

    private static List<InlineKeyboardButton> createQuestionButtons(Page<Question> questionsPage) {
        return questionsPage.getContent().stream()
                .map(question -> new InlineKeyboardButton(String.valueOf(question.getQuestion()))
                        .callbackData("view_question_" + question.getQuestionId()))
                .collect(Collectors.toList());
    }

    private static List<InlineKeyboardButton> createNavigationButtons(Page<Question> questionsPage) {
        int currentPage = questionsPage.getNumber();
        int totalPages = questionsPage.getTotalPages();

        InlineKeyboardButton previousButton = new InlineKeyboardButton("\t←").callbackData("previous_page");
        InlineKeyboardButton nextButton = new InlineKeyboardButton("\t→")
                .callbackData("next_page");

        if (totalPages <= 1) {
            return List.of(previousButton, nextButton);
        } else if (currentPage == 0) {
            return List.of(nextButton);
        } else if (currentPage == totalPages - 1) {
            return List.of(previousButton);
        } else {
            return List.of(previousButton, nextButton);
        }
    }
}
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
    // для клавиатуры нужно передать в updateParameters(int userId, int firstNumber, int numberOfButton)
    // int userId чтобы можно было правильно отработать нажатие другим админом (заигнорить)

    private static int pageSize = 5;
    private static Sort.Direction sortDirection = Sort.Direction.ASC;

    private static QuestionPaginationRepository questionPaginationRepository;

    public static void setQuestionPaginationRepository(QuestionPaginationRepository questionPaginationRepository) {
        ViewQuestionsKeyboardCreator.questionPaginationRepository = questionPaginationRepository;
    }

    private static Map<Integer, Integer> userPageMap = new HashMap<>();
    public static InlineKeyboardMarkup createQuestionsKeyboard(int userId) {
        int page = userPageMap.getOrDefault(userId, 0);
        PageRequest pageRequest = createPageRequest(page);
        Page<Question> questionsPage = questionPaginationRepository.findAll(pageRequest);

        List<InlineKeyboardButton> questionButtons = createQuestionButtons(questionsPage);

        List<InlineKeyboardButton> navigationButtons = createNavigationButtons(questionsPage);

        InlineKeyboardButton[][] keyboardRows = { questionButtons.toArray(new InlineKeyboardButton[0]), navigationButtons.toArray(new InlineKeyboardButton[0]) };
        return new InlineKeyboardMarkup(keyboardRows);
    }

    private static PageRequest createPageRequest(int page) {
        Sort.Direction sortDirection = Sort.Direction.ASC;
        return PageRequest.of(page, pageSize, sortDirection, "id");
    }

    private static List<InlineKeyboardButton> createQuestionButtons(Page<Question> questionsPage) {
        return questionsPage.getContent().stream()
                .map(question -> new InlineKeyboardButton(String.valueOf(question.getQuestionId()))
                        .callbackData("view_question_" + question.getQuestionId()))
                .collect(Collectors.toList());
    }

    private static List<InlineKeyboardButton> createNavigationButtons(Page<Question> questionsPage) {
        int currentPage = questionsPage.getNumber();
        int totalPages = questionsPage.getTotalPages();

        InlineKeyboardButton previousButton = new InlineKeyboardButton("<< Previous").callbackData("previous_page");
        InlineKeyboardButton nextButton = new InlineKeyboardButton("Next >>")
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
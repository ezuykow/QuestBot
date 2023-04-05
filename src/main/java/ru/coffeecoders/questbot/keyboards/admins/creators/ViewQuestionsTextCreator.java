package ru.coffeecoders.questbot.keyboards.admins.creators;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Question;

@Component
public class ViewQuestionsTextCreator {

    public static String createQuestionText(int messageId) {
        int page = ViewQuestionsKeyboardCreator.userPageMap.getOrDefault(messageId, 0);
        PageRequest pageRequest = ViewQuestionsKeyboardCreator.createPageRequest(page);
        Page<Question> questionsPage = questionPaginationRepository.findAll(pageRequest);

        return createQuestionsPage(questionsPage);
    }
    private static String createQuestionsPage(Page<Question> questionsPage) {
        StringBuilder sb = new StringBuilder();
        for (Question question : questionsPage.getContent()) {
            sb.append(question.toString()).append("\n\n");
        }
        return sb.toString();
    }

}

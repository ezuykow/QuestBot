package ru.coffeecoders.questbot.viewers.questions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.msg.senders.MessageSender;
import ru.coffeecoders.questbot.services.QuestionService;

import java.util.List;

/**
 * @author ezuykow
 */
@Component
public class QuestionsViewer {

    @Value("${viewer.questions.page.size}")
    private int defaultPageSize;

    private final QuestionService questionService;
    private final MessageSender msgSender;

    private List<Question> questions;

    public QuestionsViewer(QuestionService questionService, MessageSender msgSender) {
        this.questionService = questionService;
        this.msgSender = msgSender;
    }

    public void viewQuestions(long chatId) {
        questions = questionService.findAll();
        int pageSize = Math.min(defaultPageSize, questions.size());
        QuestionsViewerPage page = QuestionsViewerPage.createPage(questions, pageSize, 0);
        msgSender.send(chatId, page.getText(), page.getKeyboard());
    }
}

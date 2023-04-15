package ru.coffeecoders.questbot.viewers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.models.QuestionsViewerPage;
import ru.coffeecoders.questbot.senders.MessageSender;
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

    public void switchPageToPrevious(ExtendedUpdate update, String data) {
        final int firstIndexShowed = Integer.parseInt(data.substring(data.lastIndexOf(".") + 1));
        QuestionsViewerPage newPage = QuestionsViewerPage.createPage(questions, defaultPageSize,
                firstIndexShowed - defaultPageSize);
        msgSender.edit(update.getCallbackMessageChatId(), update.getCallbackMessageId(),
                newPage.getText(), newPage.getKeyboard());
    }

    public void switchPageToNext(ExtendedUpdate update, String data) {
        final int lastIndexShowed = Integer.parseInt(data.substring(data.lastIndexOf(".") + 1));
        final int newPageSize = Math.min(defaultPageSize, questions.size() - (lastIndexShowed + 1));
        QuestionsViewerPage newPage = QuestionsViewerPage.createPage(questions, newPageSize,
                lastIndexShowed + 1);
        msgSender.edit(update.getCallbackMessageChatId(), update.getCallbackMessageId(),
                newPage.getText(), newPage.getKeyboard());
    }
}

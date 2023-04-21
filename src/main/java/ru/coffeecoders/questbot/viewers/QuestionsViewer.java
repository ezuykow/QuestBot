package ru.coffeecoders.questbot.viewers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Question;
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
    private final QuestionInfoViewer questionInfoViewer;
    private final MessageSender msgSender;

    private List<Question> questions;
    private int pagesCount;
    private int lastShowedFirstIndex;

    public QuestionsViewer(QuestionService questionService, QuestionInfoViewer questionInfoViewer, MessageSender msgSender) {
        this.questionService = questionService;
        this.questionInfoViewer = questionInfoViewer;
        this.msgSender = msgSender;
    }

    /**
     * Собирает "страницу" {@link QuestionsViewerPage} и вызывает метод
     * {@link MessageSender#send} для отображения "страницы" вопросов
     */
    public void viewQuestions(long chatId) {
        refreshQuestionsList();
        QuestionsViewerPage page = createPage(defaultPageSize);
        msgSender.send(chatId, page.getText(), page.getKeyboard());
    }

    /**
     * "Перелистывает" страницу отображения вопросов на предыдущую
     * @param data данные из CallbackQuery
     */
    public void switchPageToPrevious(long chatId, int msgId, String data) {
        final int firstIndexShowed = Integer.parseInt(data.substring(data.lastIndexOf(".") + 1));
        QuestionsViewerPage newPage = QuestionsViewerPage.createPage(questions, defaultPageSize,
                firstIndexShowed - defaultPageSize, pagesCount);
        msgSender.edit(chatId, msgId, newPage.getText(), newPage.getKeyboard());
    }

    /**
     * "Перелистывает" страницу отображения вопросов на следующую
     * @param data данные из CallbackQuery
     */
    public void switchPageToNext(long chatId, int msgId, String data) {
        final int lastIndexShowed = Integer.parseInt(data.substring(data.lastIndexOf(".") + 1));
        final int newPageSize = Math.min(defaultPageSize, questions.size() - (lastIndexShowed + 1));
        QuestionsViewerPage newPage = QuestionsViewerPage.createPage(questions, newPageSize,
                lastIndexShowed + 1, pagesCount);
        msgSender.edit(chatId, msgId, newPage.getText(), newPage.getKeyboard());
    }

    /**
     * Вызывает метод {@link QuestionInfoViewer#showQuestionInfo} для отображения информации о вопросе
     * @param data данные из CallbackQuery
     */
    public void showQuestionInfo(long chatId, int msgId, String data) {
        String[] parts = data.split("\\.");
        lastShowedFirstIndex = Integer.parseInt(parts[parts.length - 1]);
        int targetQuestionIdx = Integer.parseInt(parts[2]);
        questionInfoViewer.showQuestionInfo(chatId, msgId, questions.get(targetQuestionIdx));
    }

    /**
     * Возвращает отображение "страницы" с вопросами из "страницы" с отображением информации о вопросе
     */
    public void backFromQuestionInfo(long chatId, int msgId) {
        refreshQuestionsList();
        int pageSize = Math.min(defaultPageSize, questions.size() - lastShowedFirstIndex);
        QuestionsViewerPage page = createPage(pageSize);
        msgSender.edit(chatId, msgId, page.getText(), page.getKeyboard());
    }

    /**
     * "Закрывает" "страницу" с вопросами - т.е. удаляет сообщение из чата
     */
    public void deleteView(long chatId, int msgId) {
        msgSender.sendDelete(chatId, msgId);
    }

    private void refreshQuestionsList() {
        questions = questionService.findAll();
    }

    private QuestionsViewerPage createPage(int pageSize) {
        pagesCount = questions.size() / defaultPageSize;
        pagesCount = (questions.size() % defaultPageSize == 0) ? pagesCount : pagesCount + 1;
        return QuestionsViewerPage.createPage(questions, pageSize, lastShowedFirstIndex, pagesCount);
    }
}

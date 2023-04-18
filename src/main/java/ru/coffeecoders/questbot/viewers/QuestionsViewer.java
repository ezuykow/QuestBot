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
    private final QuestionInfoViewer questionInfoViewer;
    private final MessageSender msgSender;

    private List<Question> questions;
    private int lastShowedFirstIndex;

    public QuestionsViewer(QuestionService questionService, QuestionInfoViewer questionInfoViewer, MessageSender msgSender) {
        this.questionService = questionService;
        this.questionInfoViewer = questionInfoViewer;
        this.msgSender = msgSender;
    }

    /**
     * Собирает "страницу" {@link QuestionsViewerPage} и вызывает метод
     * {@link MessageSender#send} для отображения "страницы" вопросов
     * @param chatId id чата, в который отобразить вопросы
     */
    public void viewQuestions(long chatId) {
        refreshQuestionsList();
        lastShowedFirstIndex = 0;
        int pageSize = Math.min(defaultPageSize, questions.size());
        QuestionsViewerPage page = QuestionsViewerPage.createPage(questions, pageSize, lastShowedFirstIndex);
        msgSender.send(chatId, page.getText(), page.getKeyboard());
    }

    /**
     * "Перелистывает" страницу отображения вопросов на предыдущую
     * @param update апдейт с CallbackQuery
     * @param data данные из CallbackQuery
     */
    public void switchPageToPrevious(ExtendedUpdate update, String data) {
        final int firstIndexShowed = Integer.parseInt(data.substring(data.lastIndexOf(".") + 1));
        QuestionsViewerPage newPage = QuestionsViewerPage.createPage(questions, defaultPageSize,
                firstIndexShowed - defaultPageSize);
        msgSender.edit(update.getCallbackMessageChatId(), update.getCallbackMessageId(),
                newPage.getText(), newPage.getKeyboard());
    }

    /**
     * "Перелистывает" страницу отображения вопросов на следующую
     * @param update апдейт с CallbackQuery
     * @param data данные из CallbackQuery
     */
    public void switchPageToNext(ExtendedUpdate update, String data) {
        final int lastIndexShowed = Integer.parseInt(data.substring(data.lastIndexOf(".") + 1));
        final int newPageSize = Math.min(defaultPageSize, questions.size() - (lastIndexShowed + 1));
        QuestionsViewerPage newPage = QuestionsViewerPage.createPage(questions, newPageSize,
                lastIndexShowed + 1);
        msgSender.edit(update.getCallbackMessageChatId(), update.getCallbackMessageId(),
                newPage.getText(), newPage.getKeyboard());
    }

    /**
     * Вызывает метод {@link QuestionInfoViewer#showQuestionInfo} для отображения информации о вопросе
     * @param update апдейт с CallbackQuery
     * @param data данные из CallbackQuery
     */
    public void showQuestionInfo(ExtendedUpdate update, String data) {
        String[] parts = data.split("\\.");
        lastShowedFirstIndex = Integer.parseInt(parts[parts.length - 1]);
        int targetQuestionIdx = Integer.parseInt(parts[2]);
        questionInfoViewer.showQuestionInfo(update, questions.get(targetQuestionIdx));
    }

    /**
     * Возвращает отображение "страницы" с вопросами из "страницы" с отображением информации о вопросе
     * @param update апдейт с CallbackQuery
     */
    public void backFromQuestionInfo(ExtendedUpdate update) {
        refreshQuestionsList();
        int pageSize = Math.min(defaultPageSize, questions.size() - lastShowedFirstIndex);
        QuestionsViewerPage page = QuestionsViewerPage.createPage(questions, pageSize, lastShowedFirstIndex);
        msgSender.edit(update.getCallbackMessageChatId(), update.getCallbackMessageId(),
                page.getText(), page.getKeyboard());
    }

    /**
     * "Закрывает" "страницу" с вопросами - т.е. удаляет сообщение из чата
     * @param update апдейт с CallbackQuery
     */
    public void deleteView(ExtendedUpdate update) {
        msgSender.sendDelete(update.getCallbackMessageChatId(), update.getCallbackMessageId());
    }

    private void refreshQuestionsList() {
        questions = questionService.findAll();
    }
}

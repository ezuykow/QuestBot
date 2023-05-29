package ru.coffeecoders.questbot.viewers;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.exceptions.NonExistentQuestion;
import ru.coffeecoders.questbot.managers.BlockingManager;
import ru.coffeecoders.questbot.managers.RestrictingManager;
import ru.coffeecoders.questbot.messages.MessageBuilder;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.models.QuestionInfoPage;
import ru.coffeecoders.questbot.models.QuestionsViewerPage;
import ru.coffeecoders.questbot.properties.PropertyService;
import ru.coffeecoders.questbot.services.QuestionGroupService;
import ru.coffeecoders.questbot.services.QuestionService;

import java.util.List;

import static java.lang.Math.min;

/**
 * @author ezuykow
 */
@Component
public class QuestionsViewer {

    private final QuestionService questionService;
    private final QuestionGroupService questionGroupService;
    private final BlockingManager blockingManager;
    private final RestrictingManager restrictingManager;
    private final MessageSender msgSender;
    private final Messages messages;
    private final MessageBuilder messageBuilder;
    private final PropertyService propertyService;

    private int defaultPageSize;
    private List<Question> questions;
    private int lastShowedFirstIndex;

    public QuestionsViewer(QuestionService questionService, QuestionGroupService questionGroupService,
                           BlockingManager blockingManager, RestrictingManager restrictingManager,
                           MessageSender msgSender, Messages messages, MessageBuilder messageBuilder,
                           PropertyService propertyService)
    {
        this.questionService = questionService;
        this.questionGroupService = questionGroupService;
        this.blockingManager = blockingManager;
        this.restrictingManager = restrictingManager;
        this.msgSender = msgSender;
        this.messages = messages;
        this.messageBuilder = messageBuilder;
        this.propertyService = propertyService;
    }

    //-----------------API START-----------------

    /**
     * Вызывает метод
     * {@link QuestionsViewer#validateAndCreateView} c {@code msgId = -1} и {@code pageSize = defaultPageSize}
     * для отображения "страницы" вопросов
     * @param chatId id чата
     * @author ezuykow
     */
    public void viewQuestions(long chatId) {
        defaultPageSize = propertyService.getDefaultPageSize();
        refreshQuestionsList();
        validateAndCreateView(chatId, -1, defaultPageSize, 0);
    }

    /**
     * "Перелистывает" страницу отображения вопросов на предыдущую
     * @param chatId id чата
     * @param msgId id сообщения, в котором отображался список вопросов
     * @param data данные из CallbackQuery
     * @author ezuykow
     */
    public void switchPageToPrevious(long chatId, int msgId, String data) {
        final int firstIndexShowed = Integer.parseInt(data.substring(data.lastIndexOf(".") + 1));
        QuestionsViewerPage newPage = createPage(defaultPageSize, firstIndexShowed - defaultPageSize);
        msgSender.edit(chatId, msgId, newPage.getText(), newPage.getKeyboard());
    }

    /**
     * "Перелистывает" страницу отображения вопросов на следующую
     * @param chatId id чата
     * @param msgId id сообщения, в котором отображался список вопросов
     * @param data данные из CallbackQuery
     * @author ezuykow
     */
    public void switchPageToNext(long chatId, int msgId, String data) {
        final int lastIndexShowed = Integer.parseInt(data.substring(data.lastIndexOf(".") + 1));
        final int newPageSize = min(defaultPageSize, questions.size() - (lastIndexShowed + 1));
        QuestionsViewerPage newPage = createPage(newPageSize, lastIndexShowed + 1);
        msgSender.edit(chatId, msgId, newPage.getText(), newPage.getKeyboard());
    }

    /**
     * Собирает "страницу" отображения вопроса {@link QuestionInfoPage} и отправляет
     * в метод {@link MessageSender#edit} для отображения вопроса
     * @param chatId id чата
     * @param msgId id сообщения, в котором отображался список вопросов
     * @param data данные из CallbackQuery
     * @author ezuykow
     */
    public void showQuestionInfo(long chatId, int msgId, String data) {
        String[] parts = data.split("\\.");
        lastShowedFirstIndex = Integer.parseInt(parts[parts.length - 1]);
        int targetQuestionIdx = Integer.parseInt(parts[2]);
        QuestionInfoPage page = QuestionInfoPage.createPage(questions.get(targetQuestionIdx));
        msgSender.edit(chatId, msgId, page.getText(), page.getKeyboard());
    }

    /**
     * Удаляет вопрос с id из {@code data} и вызывает {@link QuestionsViewer#backFromQuestionInfo}
     * @param chatId id чата
     * @param msgId id сообщения, в котором отображался вопрос
     * @param data данные калбака
     * @author ezuykow
     */
    public void deleteQuestion(long chatId, int msgId, String data) {
        int questionId = Integer.parseInt(data.substring(data.lastIndexOf(".") + 1));
        deleteQuestion(questionId);
        backFromQuestionInfo(chatId, msgId);
    }

    /**
     * Возвращает отображение "страницы" с вопросами из "страницы" с отображением информации о вопросе
     * @param chatId id чата
     * @param msgId id сообщения, в котором отображалась информация о вопросе
     * @author ezuykow
     */
    public void backFromQuestionInfo(long chatId, int msgId) {
        refreshQuestionsList();
        int pageSize = min(defaultPageSize, questions.size() - lastShowedFirstIndex);
        validateAndCreateView(chatId, msgId, pageSize, lastShowedFirstIndex);
    }

    /**
     * "Закрывает" "страницу" с вопросами - т.е. удаляет сообщение из чата
     * @param chatId id чата
     * @param msgId id сообщения, в котором отображался список вопросов
     * @author ezuykow
     */
    public void deleteView(long chatId, int msgId) {
        unblockAndUnrestrictChat(chatId);
        msgSender.sendDelete(chatId, msgId);
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void validateAndCreateView(long chatId, int msgId, int pageSize, int startIndex) {
        if (questions.isEmpty()) {
            msgSender.sendDelete(chatId, msgId);
            msgSender.send(chatId,
                    messageBuilder.build(messages.emptyList(), chatId));
            unblockAndUnrestrictChat(chatId);
        } else {
            createView(chatId, msgId, pageSize, startIndex);
        }
    }

    /**
     * @author ezuykow
     */
    private void createView(long chatId, int msgId, int pageSize, int startIndex) {
        QuestionsViewerPage page = createPage(pageSize, startIndex);
        if (msgId == -1) {
            msgSender.send(chatId, page.getText(), page.getKeyboard());
        } else {
            msgSender.edit(chatId, msgId, page.getText(), page.getKeyboard());
        }
    }

    /**
     * @author ezuykow
     */
    private void refreshQuestionsList() {
        questions = questionService.findAll();
    }

    /**
     * @author ezuykow
     */
    private QuestionsViewerPage createPage(int pageSize, int startIndex) {
        int pagesCount = questions.size() / defaultPageSize;
        pagesCount = (questions.size() % defaultPageSize == 0) ? pagesCount : pagesCount + 1;
        return QuestionsViewerPage.createPage(questions, pageSize, startIndex, pagesCount, defaultPageSize);
    }

    /**
     * @author ezuykow
     */
    private void unblockAndUnrestrictChat(long chatId) {
        restrictingManager.unRestrictMembers(chatId);
        blockingManager.unblockAdminChat(chatId, messages.endQuestionView());
    }

    /**
     * @author ezuykow
     */
    public void deleteQuestion(int questionId) {
        questionService.findById(questionId).ifPresentOrElse(
                q -> {
                    questionService.delete(q);
                    questionGroupService.deleteQuestionGroupIfNoQuestionsWithIt(q.getGroup());
                },
                NonExistentQuestion::new);
    }
}

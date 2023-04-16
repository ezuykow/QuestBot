package ru.coffeecoders.questbot.managers.callbacks;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.viewers.QuestionsViewer;

/**
 * @author ezuykow
 */
@Component
public class QuestionViewerCallbackManager {

    private static final String PREVIOUS_PAGE_REGEXP = "QuestionViewer.Switch page to previous.*";
    private static final String NEXT_PAGE_REGEXP = "QuestionViewer.Switch page to next.*";
    private static final String SHOW_QUESTION_REGEXP = "QuestionViewer.Taken index.*";
    private static final String DELETE_MESSAGE_REGEXP = "QuestionViewer.Delete message";
    private static final String QUESTION_INFO_BACK_REGEXP = "QuestionViewer.QuestionInfo.Back";

    private final QuestionsViewer questionsViewer;

    public QuestionViewerCallbackManager(QuestionsViewer questionsViewer) {
        this.questionsViewer = questionsViewer;
    }

    /**
     * Вызывает необходимый метод {@link QuestionsViewer}, исходя из {@code data}
     * @param update апдейт с CallbackQuery
     * @param data данные CallbackQuery
     */
    public void manageCallback(ExtendedUpdate update, String data) {
        if (data.matches(PREVIOUS_PAGE_REGEXP)) {
            questionsViewer.switchPageToPrevious(update, data);
        }
        if (data.matches(NEXT_PAGE_REGEXP)) {
            questionsViewer.switchPageToNext(update, data);
        }
        if (data.matches(SHOW_QUESTION_REGEXP)) {
            questionsViewer.showQuestionInfo(update, data);
        }
        if (data.matches(DELETE_MESSAGE_REGEXP)) {
            questionsViewer.deleteView(update);
        }
        if (data.matches(QUESTION_INFO_BACK_REGEXP)) {
            questionsViewer.backFromQuestionInfo(update);
        }
    }
}

package ru.coffeecoders.questbot.managers.callbacks;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.validators.ChatAndUserValidator;
import ru.coffeecoders.questbot.viewers.QuestionsViewer;

/**
 * @author ezuykow
 */
@Component
public class QuestionViewerCallbackManager {

    private enum Action {
        PREVIOUS_PAGE("QuestionViewer.Switch page to previous.*"),
        NEXT_PAGE("QuestionViewer.Switch page to next.*"),
        SHOW_QUESTION("QuestionViewer.Taken index.*"),
        DELETE_MESSAGE("QuestionViewer.Delete message"),
        BACK_FROM_QUESTION_INFO("QuestionViewer.QuestionInfo.Back"),
        UNKNOWN("");

        private final String dataRegexp;

        Action(String dataRegexp) {
            this.dataRegexp = dataRegexp;
        }
    }

    private final QuestionsViewer questionsViewer;
    private final ChatAndUserValidator validator;

    public QuestionViewerCallbackManager(QuestionsViewer questionsViewer, ChatAndUserValidator validator) {
        this.questionsViewer = questionsViewer;
        this.validator = validator;
    }

    /**
     * Вызывает необходимый метод {@link QuestionsViewer}, исходя из {@code data}
     * @param data данные CallbackQuery
     */
    public void manageCallback(long senderUserId, long chatId, int msgId, String data) {
        if (validator.isBlockedAdmin(chatId, senderUserId) || validator.isOwner(senderUserId)) {
            performCallback(chatId, msgId, data);
        }
    }

    //-----------------API END-----------------

    private void performCallback(long chatId, int msgId, String data) {
        switch (findAction(data)) {
            case PREVIOUS_PAGE -> questionsViewer.switchPageToPrevious(chatId, msgId, data);
            case NEXT_PAGE -> questionsViewer.switchPageToNext(chatId, msgId, data);
            case SHOW_QUESTION -> questionsViewer.showQuestionInfo(chatId, msgId, data);
            case DELETE_MESSAGE -> questionsViewer.deleteView(chatId, msgId);
            case BACK_FROM_QUESTION_INFO -> questionsViewer.backFromQuestionInfo(chatId, msgId);
            case UNKNOWN -> {} //Игнорируем неизвестный калбак
        }
    }

    private Action findAction(String data) {
        for (Action a : Action.values()) {
            if (data.matches(a.dataRegexp)) {
                return a;
            }
        }
        return Action.UNKNOWN;
    }
}

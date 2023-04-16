package ru.coffeecoders.questbot.managers;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.managers.callbacks.QuestionViewerCallbackManager;
import ru.coffeecoders.questbot.models.ExtendedUpdate;

/**
 * @author ezuykow
 */
@Component
public class CallbackQueryManager {

    //TODO Заменить константы и развилки на private enum и switch как в QuestionViewerCallbackManager

    private static final String QUESTION_VIEWER_CALLBACK_REGEXP = "QuestionViewer.*";

    private final QuestionViewerCallbackManager questionViewerCallbackManager;

    public CallbackQueryManager(QuestionViewerCallbackManager questionViewerCallbackManager) {
        this.questionViewerCallbackManager = questionViewerCallbackManager;
    }

    /**
     * По данным калбака передает апдейт конкретному менеджеру
     * @param update апдейт с CallbackQuery
     * @see QuestionViewerCallbackManager
     */
    public void manageCallback(ExtendedUpdate update) {
        final String data = update.getCallbackQueryData();

        if (data.matches(QUESTION_VIEWER_CALLBACK_REGEXP)) {
            questionViewerCallbackManager.manageCallback(update, data);
        }

    }
}

package ru.coffeecoders.questbot.managers.callbacks;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.models.ExtendedUpdate;

/**
 * @author ezuykow
 */
@Component
public class CallbackQueryManager {

    //TODO Заменить константы и развилки на private enum и switch как в QuestionViewerCallbackManager

    private static final String QUESTION_VIEWER_CALLBACK_REGEXP = "QuestionViewer.*";
    private static final String PROMOTE_USER_CALLBACK_REGEXP = "PromoteUser.*";

    private final QuestionViewerCallbackManager questionViewerCallbackManager;
    private final PromoteUserCallbackManager promoteUserCallbackManager;

    public CallbackQueryManager(QuestionViewerCallbackManager questionViewerCallbackManager,
                                PromoteUserCallbackManager promoteUserCallbackManager)
    {
        this.questionViewerCallbackManager = questionViewerCallbackManager;
        this.promoteUserCallbackManager = promoteUserCallbackManager;
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

        if (data.matches(PROMOTE_USER_CALLBACK_REGEXP)) {
            promoteUserCallbackManager.manageCallback(update, data);
        }

    }
}

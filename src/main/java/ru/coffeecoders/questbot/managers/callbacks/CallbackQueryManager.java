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
    private static final String DEMOTE_USER_CALLBACK_REGEXP = "DemoteUser.*";

    private final QuestionViewerCallbackManager questionViewerCallbackManager;
    private final PromoteUserCallbackManager promoteUserCallbackManager;
    private final DemoteUserCallbackManager demoteUserCallbackManager;

    public CallbackQueryManager(QuestionViewerCallbackManager questionViewerCallbackManager,
                                PromoteUserCallbackManager promoteUserCallbackManager,
                                DemoteUserCallbackManager demoteUserCallbackManager)
    {
        this.questionViewerCallbackManager = questionViewerCallbackManager;
        this.promoteUserCallbackManager = promoteUserCallbackManager;
        this.demoteUserCallbackManager = demoteUserCallbackManager;
    }

    /**
     * По данным калбака передает апдейт конкретному менеджеру
     * @param update апдейт с CallbackQuery
     * @see QuestionViewerCallbackManager
     */
    public void manageCallback(ExtendedUpdate update) {
        final long senderUserId = update.getCallbackFromUserId();
        final long chatId = update.getCallbackMessageChatId();
        final int msgId = update.getCallbackMessageId();
        final String data = update.getCallbackQueryData();

        if (data.matches(QUESTION_VIEWER_CALLBACK_REGEXP)) {
            questionViewerCallbackManager.manageCallback(chatId, msgId, data);
        }
        if (data.matches(PROMOTE_USER_CALLBACK_REGEXP)) {
            promoteUserCallbackManager.manageCallback(senderUserId, chatId, msgId, data);
        }
        if (data.matches(DEMOTE_USER_CALLBACK_REGEXP)) {
            demoteUserCallbackManager.manageCallback(senderUserId, chatId, msgId, data);
        }
    }
}

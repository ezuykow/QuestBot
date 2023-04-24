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
    private static final String GAME_VIEWER_CALLBACK_REGEXP = "GameViewer.*";
    private static final String PROMOTE_USER_CALLBACK_REGEXP = "PromoteUser.*";
    private static final String DEMOTE_USER_CALLBACK_REGEXP = "DemoteUser.*";
    private static final String QUESTION_GROUP_SELECTED_REGEXP = "QuestionGroupSelected.*";

    private final QuestionViewerCallbackManager questionViewerCallbackManager;
    private final GamesViewerCallbackManager gamesViewerCallbackManager;
    private final PromoteUserCallbackManager promoteUserCallbackManager;
    private final DemoteUserCallbackManager demoteUserCallbackManager;
    private final NewGameCreatingCallbackManager newGameCreatingCallbackManager;

    public CallbackQueryManager(QuestionViewerCallbackManager questionViewerCallbackManager,
                                GamesViewerCallbackManager gamesViewerCallbackManager,
                                PromoteUserCallbackManager promoteUserCallbackManager,
                                DemoteUserCallbackManager demoteUserCallbackManager,
                                NewGameCreatingCallbackManager newGameCreatingCallbackManager)
    {
        this.questionViewerCallbackManager = questionViewerCallbackManager;
        this.gamesViewerCallbackManager = gamesViewerCallbackManager;
        this.promoteUserCallbackManager = promoteUserCallbackManager;
        this.demoteUserCallbackManager = demoteUserCallbackManager;
        this.newGameCreatingCallbackManager = newGameCreatingCallbackManager;
    }

    //-----------------API START-----------------

    /**
     * По данным калбака передает апдейт конкретному менеджеру
     * @param update апдейт с CallbackQuery
     * @author ezuykow
     */
    public void manageCallback(ExtendedUpdate update) {
        final long senderUserId = update.getCallbackFromUserId();
        final long chatId = update.getCallbackMessageChatId();
        final int msgId = update.getCallbackMessageId();
        final String data = update.getCallbackQueryData();
        final String id = update.getCallbackQueryId();

        if (data.matches(QUESTION_VIEWER_CALLBACK_REGEXP)) {
            questionViewerCallbackManager.manageCallback(senderUserId, chatId, msgId, data);
        }
        if (data.matches(GAME_VIEWER_CALLBACK_REGEXP)) {
            gamesViewerCallbackManager.manageCallback(id, senderUserId, chatId, msgId, data);
        }
        if (data.matches(PROMOTE_USER_CALLBACK_REGEXP)) {
            promoteUserCallbackManager.manageCallback(senderUserId, chatId, msgId, data);
        }
        if (data.matches(DEMOTE_USER_CALLBACK_REGEXP)) {
            demoteUserCallbackManager.manageCallback(senderUserId, chatId, msgId, data);
        }
        if (data.matches(QUESTION_GROUP_SELECTED_REGEXP)) {
            newGameCreatingCallbackManager.manageCallback(senderUserId, chatId, msgId, data);
        }
    }

    //-----------------API END-----------------

}

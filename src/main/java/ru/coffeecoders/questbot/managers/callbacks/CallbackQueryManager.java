package ru.coffeecoders.questbot.managers.callbacks;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.properties.PropertiesViewerCallbackManager;

/**
 * @author ezuykow
 */
@Component
public class CallbackQueryManager {

    private enum Manager {
        QUESTION_VIEWER("QuestionViewer.*"),
        GAME_VIEWER("GameViewer.*"),
        PROMOTE_USER("PromoteUser.*"),
        DEMOTE_USER("DemoteUser.*"),
        QUESTION_GROUP_SELECTED("QuestionGroupSelected.*"),
        PREPARE_GAME("PrepareGame.*"),
        PROPERTIES("PropertiesViewer.*"),
        UNKNOWN("");

        private final String regexp;

        Manager(String regexp) {
            this.regexp = regexp;
        }
    }

    private final QuestionViewerCallbackManager questionViewerCallbackManager;
    private final GamesViewerCallbackManager gamesViewerCallbackManager;
    private final PromoteUserCallbackManager promoteUserCallbackManager;
    private final DemoteUserCallbackManager demoteUserCallbackManager;
    private final NewGameCreatingCallbackManager newGameCreatingCallbackManager;
    private final PrepareGameCallbackManager prepareGameCallbackManager;
    private final PropertiesViewerCallbackManager propertiesViewerCallbackManager;

    public CallbackQueryManager(QuestionViewerCallbackManager questionViewerCallbackManager,
                                GamesViewerCallbackManager gamesViewerCallbackManager,
                                PromoteUserCallbackManager promoteUserCallbackManager,
                                DemoteUserCallbackManager demoteUserCallbackManager,
                                NewGameCreatingCallbackManager newGameCreatingCallbackManager,
                                PrepareGameCallbackManager prepareGameCallbackManager,
                                PropertiesViewerCallbackManager propertiesViewerCallbackManager)
    {
        this.questionViewerCallbackManager = questionViewerCallbackManager;
        this.gamesViewerCallbackManager = gamesViewerCallbackManager;
        this.promoteUserCallbackManager = promoteUserCallbackManager;
        this.demoteUserCallbackManager = demoteUserCallbackManager;
        this.newGameCreatingCallbackManager = newGameCreatingCallbackManager;
        this.prepareGameCallbackManager = prepareGameCallbackManager;
        this.propertiesViewerCallbackManager = propertiesViewerCallbackManager;
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
        switch (findManager(data)) {
            case QUESTION_VIEWER -> questionViewerCallbackManager.manageCallback(senderUserId, chatId, msgId, data);
            case GAME_VIEWER -> gamesViewerCallbackManager.manageCallback(id, senderUserId, chatId, msgId, data);
            case PROMOTE_USER -> promoteUserCallbackManager.manageCallback(senderUserId, chatId, msgId, data);
            case DEMOTE_USER -> demoteUserCallbackManager.manageCallback(senderUserId, chatId, msgId, data);
            case QUESTION_GROUP_SELECTED -> newGameCreatingCallbackManager.manageCallback(senderUserId, chatId, msgId, data);
            case PREPARE_GAME -> prepareGameCallbackManager.manageCallback(id, senderUserId, chatId, msgId, data);
            case PROPERTIES -> propertiesViewerCallbackManager.manageCallback(senderUserId, chatId, msgId, data);
            case UNKNOWN -> {} //Игнорируем неизвестный калбак
        }
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private Manager findManager(String data) {
        for (Manager a : Manager.values()) {
            if (data.matches(a.regexp)) {
                return a;
            }
        }
        return Manager.UNKNOWN;
    }
}

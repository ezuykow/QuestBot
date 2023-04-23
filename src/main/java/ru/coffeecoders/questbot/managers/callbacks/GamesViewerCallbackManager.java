package ru.coffeecoders.questbot.managers.callbacks;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.validators.ChatAndUserValidator;
import ru.coffeecoders.questbot.viewers.GamesViewer;

/**
 * @author ezuykow
 */
@Component
public class GamesViewerCallbackManager {

    private enum Action {
        SHOW_GAME("GameViewer.Taken game.*"),
        DELETE("GameViewer.Game info.Delete.*"),
        BACK_FROM_INFO("GameViewer.Game info.Back"),
        CLOSE("GameViewer.Close"),
        UNKNOWN("");

        private final String dataRegexp;

        Action(String dataRegexp) {
            this.dataRegexp = dataRegexp;
        }

    }

    private final GamesViewer gamesViewer;
    private final ChatAndUserValidator validator;

    public GamesViewerCallbackManager(GamesViewer gamesViewer, ChatAndUserValidator validator) {
        this.gamesViewer = gamesViewer;
        this.validator = validator;
    }

    //-----------------API START-----------------

    public void manageCallback(String callbackId, long senderUserId, long chatId, int msgId, String data) {
        if (validator.isBlockedAdmin(chatId, senderUserId) || validator.isOwner(senderUserId)) {
            performCallback(callbackId, chatId, msgId, data);
        }
    }

    //-----------------API END-----------------

    private void performCallback(String callbackId, long chatId, int msgId, String data) {
        switch (findActions(data)) {
            case SHOW_GAME -> gamesViewer.showGame(chatId, msgId, data);
            case DELETE -> gamesViewer.deleteGame(callbackId, chatId, msgId, data);
            case BACK_FROM_INFO -> gamesViewer.backFromInfo(chatId, msgId);
            case CLOSE -> gamesViewer.closeView(chatId, msgId);
            case UNKNOWN -> {} //Игнорируем неизвестный калбак
        }
    }

    private Action findActions(String data) {
        for (Action a : Action.values()) {
            if (data.matches(a.dataRegexp)) {
                return a;
            }
        }
        return Action.UNKNOWN;
    }
}

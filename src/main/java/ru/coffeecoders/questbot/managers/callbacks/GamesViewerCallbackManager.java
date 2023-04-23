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

    public void manageCallback(long senderUserId, long chatId, int msgId, String data) {
        if (validator.isBlockedAdmin(chatId, senderUserId) || validator.isOwner(senderUserId)) {
            performCallback(chatId, msgId, data);
        }
    }

    //-----------------API END-----------------

    private void performCallback(long chatId, int msgId, String data) {
        switch (findActions(data)) {
            case SHOW_GAME -> {}
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

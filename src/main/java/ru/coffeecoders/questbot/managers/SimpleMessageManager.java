package ru.coffeecoders.questbot.managers;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.actions.SimpleMessageActions;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.properties.PropertySeed;
import ru.coffeecoders.questbot.properties.viewer.PropertiesViewer;
import ru.coffeecoders.questbot.validators.GameValidator;

import java.util.Map;

/**
 * @author ezuykow
 */
@Component
public class SimpleMessageManager {

    private final SimpleMessageActions actions;
    private final NewGameManager newGameManager;
    private final GameValidator gameValidator;
    private final PropertiesViewer propertiesViewer;
    private final Map<String, PropertySeed> properties;

    public SimpleMessageManager(SimpleMessageActions actions, NewGameManager newGameManager,
                                GameValidator gameValidator, PropertiesViewer propertiesViewer,
                                Map<String, PropertySeed> properties) {
        this.actions = actions;
        this.newGameManager = newGameManager;
        this.gameValidator = gameValidator;
        this.propertiesViewer = propertiesViewer;
        this.properties = properties;
    }

    //-----------------API START-----------------

    /**
     * Обрабатываю сообщение
     * @param update апдейт
     * @author ezuykow
     */
    public void manageMessage(ExtendedUpdate update) {
        long chatId = update.getMessageChatId();
        int msgId = update.getMessageId();
        String text = update.getMessageText().trim();

        if (gameValidator.isNewGameCreating(chatId)) {
            newGameManager.manageNewGamePart(chatId, text, msgId);
        }
        if (gameValidator.isGameStarted(chatId) && text.toUpperCase().matches("\\d+.*")) {
            long senderId = update.getMessageFromUserId();
            actions.validateAnswer(chatId, text, msgId, senderId);
        }
        if (update.hasReplyToMessage()) {
            manageReplyToMessage(update);
        }
    }

    //-----------------API END-----------------

    private void manageReplyToMessage(ExtendedUpdate update) {
        String replyMsgText = update.getReplyToMessage().text();
        if (replyMsgText != null) {
            if (replyMsgText.contains(properties.get("messages.players.enterTeamCount").getActualProperty())) {
                actions.registerNewTeams(update);
            }
            if (replyMsgText.contains("новое значение параметра")) {
                propertiesViewer.performEditProperty(update, update.getMessageText());
            }
        }
    }
}

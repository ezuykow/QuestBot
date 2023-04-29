package ru.coffeecoders.questbot.managers;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.actions.SimpleMessageActions;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.validators.GameValidator;

/**
 * @author ezuykow
 */
@Component
public class SimpleMessageManager {

    private final SimpleMessageActions actions;
    private final NewGameManager newGameManager;
    private final GameValidator gameValidator;
    private final Messages messages;

    public SimpleMessageManager(SimpleMessageActions actions, NewGameManager newGameManager,
                                GameValidator gameValidator, Messages messages) {
        this.actions = actions;
        this.newGameManager = newGameManager;
        this.gameValidator = gameValidator;
        this.messages = messages;
    }

    //-----------------API START-----------------

//TODO JavaDoc, когда метод будет готов
    public void manageMessage(ExtendedUpdate update) {
        long chatId = update.getMessageChatId();
        int msgId = update.getMessageId();
        String text = update.getMessageText();

        if (gameValidator.isNewGameCreating(chatId)) {
            newGameManager.manageNewGamePart(chatId, text, msgId);
        }
        if (gameValidator.isGameStarted(chatId) && text.toUpperCase().matches("В\\d{1,2}.*")) {
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
            if (replyMsgText.contains(messages.enterTeamName())) {
                actions.registerNewTeam(update);
            }
            if (replyMsgText.contains(messages.chooseYourTeam())) {
                actions.joinTeam(update, update.getMessageText());
            }
        }
    }
}

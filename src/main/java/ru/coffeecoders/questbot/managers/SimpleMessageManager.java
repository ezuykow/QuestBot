package ru.coffeecoders.questbot.managers;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.actions.SimpleMessageActions;
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
    private final Environment env;

    public SimpleMessageManager(SimpleMessageActions actions, NewGameManager newGameManager, GameValidator gameValidator, Environment env) {
        this.actions = actions;
        this.newGameManager = newGameManager;
        this.gameValidator = gameValidator;
        this.env = env;
    }
//TODO JavaDoc, когда метод будет готов
    public void manageMessage(ExtendedUpdate update) {
        long chatId = update.getMessageChatId();
        int msgId = update.getMessageId();

        if (gameValidator.isNewGameCreating(chatId)) {
            String text = update.getMessageText();
            newGameManager.manageNewGamePart(chatId, text, msgId);
        }

        if (update.hasReplyToMessage()) {
            manageReplyToMessage(update);
        }
    }

    private void manageReplyToMessage(ExtendedUpdate update) {
        String replyMsgText = update.getReplyToMessage().text();
        if (replyMsgText != null) {
            if (replyMsgText.contains(env.getProperty("messages.players.enterTeamName", "Error"))) {
                actions.registerNewTeam(update);
            }
            if (replyMsgText.contains(env.getProperty("messages.players.chooseYourTeam", "Error"))) {
                actions.joinTeam(update, update.getMessageText());
            }
        }
    }
}

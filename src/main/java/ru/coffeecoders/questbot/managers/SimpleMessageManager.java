package ru.coffeecoders.questbot.managers;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.actions.SimpleMessageActions;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.senders.MessageSender;

/**
 * @author ezuykow
 */
@Component
public class SimpleMessageManager {

    private final SimpleMessageActions actions;
    private final MessageSender msgSender;
    private final Environment env;

    public SimpleMessageManager(SimpleMessageActions actions, MessageSender msgSender, Environment env) {
        this.actions = actions;
        this.msgSender = msgSender;
        this.env = env;
    }

    public void manageMessage(ExtendedUpdate update) {
        if (update.hasReplyToMessage()) {
            manageReplyToMessage(update);
        }
    }

    private void manageReplyToMessage(ExtendedUpdate update) {
        String replyMsgText = update.getReplyToMessage().text();
        if (replyMsgText != null) {
            if (replyMsgText.contains(env.getProperty("messages.players.enterTeamName"))) {
                actions.registerNewTeam(update);
            }
            if (replyMsgText.contains(env.getProperty("messages.players.chooseYourTeam"))) {
                actions.joinTeam(update);
            }
        }
    }
}

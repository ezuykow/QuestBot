package ru.coffeecoders.questbot.managers;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.actions.SimpleMessageActions;
import ru.coffeecoders.questbot.models.ExtendedUpdate;

/**
 * @author ezuykow
 */
@Component
public class SimpleMessageManager {

    private final SimpleMessageActions actions;

    public SimpleMessageManager(SimpleMessageActions actions) {
        this.actions = actions;
    }

    public void manageMessage(ExtendedUpdate update) {
        update.isReplyToCommand().ifPresent(cmd -> actions.performReplyToCommand(update, cmd));
    }
}

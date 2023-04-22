package ru.coffeecoders.questbot.actions;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.senders.MessageSender;

/**
 * @author ezuykow
 */
@Component
public class NewGameActions {

    private final MessageSender msgSender;
    private final Environment env;

    public NewGameActions(MessageSender msgSender, Environment env) {
        this.msgSender = msgSender;
        this.env = env;
    }

    public void createNewGameCreatingState(long chatId) {

    }


    private void requestNewGameName(long chatId) {
        msgSender.send(chatId, env.getProperty("messages.game.requestNewGameName"));
    }
}

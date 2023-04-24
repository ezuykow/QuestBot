package ru.coffeecoders.questbot.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ezuykow
 */
public class NonExistentChatMemberUpdated extends RuntimeException{

    Logger logger = LoggerFactory.getLogger(NonExistentChatMemberUpdated.class);

    public NonExistentChatMemberUpdated() {
        super();
        logger.error("DEATH/ChatMemberUpdated not found!");
    }

}

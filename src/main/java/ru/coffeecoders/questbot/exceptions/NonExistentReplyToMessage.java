package ru.coffeecoders.questbot.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ezuykow
 */
public class NonExistentReplyToMessage extends RuntimeException{

    Logger logger = LoggerFactory.getLogger(NonExistentReplyToMessage.class);

    public NonExistentReplyToMessage() {
        super();
        logger.error("DEATH/ReplyToMessage not found!");
    }

}

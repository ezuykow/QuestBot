package ru.coffeecoders.questbot.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ezuykow
 */
public class NonExistentMessage extends RuntimeException{

    Logger logger = LoggerFactory.getLogger(NonExistentMessage.class);

    public NonExistentMessage() {
        super();
        logger.error("DEATH/Message not found!");
    }

}

package ru.coffeecoders.questbot.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ezuykow
 */
public class NonExistentChat extends RuntimeException{

    Logger logger = LoggerFactory.getLogger(NonExistentChat.class);

    public NonExistentChat() {
        super();
        logger.error("DEATH/Chat not found!");
    }

}

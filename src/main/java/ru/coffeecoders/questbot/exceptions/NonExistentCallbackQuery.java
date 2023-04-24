package ru.coffeecoders.questbot.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ezuykow
 */
public class NonExistentCallbackQuery extends RuntimeException{

    Logger logger = LoggerFactory.getLogger(NonExistentCallbackQuery.class);

    public NonExistentCallbackQuery() {
        super();
        logger.error("DEATH/CallbackQuery not found!");
    }

}

package ru.coffeecoders.questbot.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ezuykow
 */
public class NonExistentQuestion extends RuntimeException{

    Logger logger = LoggerFactory.getLogger(NonExistentQuestion.class);

    public NonExistentQuestion() {
        super();
        logger.error("DEATH/Question not found!");
    }

}

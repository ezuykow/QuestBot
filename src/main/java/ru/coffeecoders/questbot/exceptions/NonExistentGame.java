package ru.coffeecoders.questbot.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ezuykow
 */
public class NonExistentGame extends RuntimeException {

    Logger logger = LoggerFactory.getLogger(NonExistentGame.class);

    public NonExistentGame() {
        super();
        logger.error("DEATH/Game not found!");
    }
}

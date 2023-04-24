package ru.coffeecoders.questbot.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ezuykow
 */
public class NonExistentDocument extends RuntimeException{

    Logger logger = LoggerFactory.getLogger(NonExistentDocument.class);

    public NonExistentDocument() {
        super();
        logger.error("DEATH/Document not found!");
    }

}

package ru.coffeecoders.questbot.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ezuykow
 */
public class NonExistentAdmin extends RuntimeException{

    Logger logger = LoggerFactory.getLogger(NonExistentAdmin.class);

    public NonExistentAdmin() {
        super();
        logger.error("DEATH/Admin not found!");
    }

}

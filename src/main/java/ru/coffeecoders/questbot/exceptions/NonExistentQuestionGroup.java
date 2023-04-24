package ru.coffeecoders.questbot.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ezuykow
 */

public class NonExistentQuestionGroup extends RuntimeException{

    Logger logger = LoggerFactory.getLogger(NonExistentQuestionGroup.class);

    public NonExistentQuestionGroup() {
        super();
        logger.error("DEATH/QuestionGroup not found!");
    }

}

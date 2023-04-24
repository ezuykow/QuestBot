package ru.coffeecoders.questbot.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author ezuykow
 */
@Component
public class ExceptionManager {

    Logger logger = LoggerFactory.getLogger(ExceptionManager.class);

    //-----------------API START-----------------

    /**
     * Логгирует сообщение исключения
     * @param e исключение
     * @author ezuykow
     */
    public void logException(Exception e) {
        logger.error("DEATH\n" +
                "☠☠☠☠☠☠☠☠☠☠☠☠☠☠☠☠☠☠☠☠☠☠ " + e.getMessage() + " ☠☠☠☠☠☠☠☠☠☠☠☠☠☠☠☠☠☠☠☠☠☠");
    }

    //-----------------API END-----------------

}

package ru.coffeecoders.questbot.managers;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.logs.LogSender;

/**
 * @author ezuykow
 */
@Component
public class ExceptionManager {

    private final LogSender logger;

    public ExceptionManager(LogSender logger) {
        this.logger = logger;
    }

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

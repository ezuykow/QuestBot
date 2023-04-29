package ru.coffeecoders.questbot.logs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author ezuykow
 */
@Component
public class LogSender {

    Logger logger = LoggerFactory.getLogger(LogSender.class);

    //-----------------API START-----------------

    /**
     * Отправляет warn-лог с сообщением
     * @param msg сообщение
     * @author ezuykow
     */
    public void warn(String msg) {
        logger.warn(msg);
    }

    /**
     * Отправляет error-лог с сообщением
     * @param msg сообщение
     * @author ezuykow
     */
    public void error(String msg) {
        logger.error(msg);
    }

    //-----------------API END-----------------

}

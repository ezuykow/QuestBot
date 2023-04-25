package ru.coffeecoders.questbot.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author ezuykow
 */
@Component
public class ApplicationShutdownManager {

    Logger logger = LoggerFactory.getLogger(ApplicationShutdownManager.class);

    private final ApplicationContext context;

    public ApplicationShutdownManager(ApplicationContext context) {
        this.context = context;
    }

    //-----------------API START-----------------

    /**
     * Закрывает контекст приложения, останавливает приложение
     * @author ezuykow
     */
    public void stopBot() {
        logger.warn("Bot has been stopped!");
        System.exit(SpringApplication.exit(context, () -> 0));
    }

    //-----------------API END-----------------

}

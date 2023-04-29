package ru.coffeecoders.questbot.managers;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.logs.LogSender;

/**
 * @author ezuykow
 */
@Component
public class ApplicationShutdownManager {


    private final ApplicationContext context;
    private final LogSender logger;

    public ApplicationShutdownManager(ApplicationContext context, LogSender logger) {
        this.context = context;
        this.logger = logger;
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

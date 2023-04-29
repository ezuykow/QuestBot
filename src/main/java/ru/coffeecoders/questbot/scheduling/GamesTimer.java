package ru.coffeecoders.questbot.scheduling;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.services.GlobalChatService;
import ru.coffeecoders.questbot.validators.GameValidator;

import java.util.List;

/**
 * @author ezuykow
 */
@Component
@EnableScheduling
public class GamesTimer {

    private static final int MIN_IN_MILLIS = 60_000;

    private final GlobalChatService globalChatService;
    private final GameValidator gameValidator;

    public GamesTimer(GlobalChatService globalChatService, GameValidator gameValidator) {
        this.globalChatService = globalChatService;
        this.gameValidator = gameValidator;
    }

    //-----------------API START-----------------

    /**
     * Прибавляет к времени, прошедшему со старта игры в чатах одну минуту и вызывает проверку на истекшее время
     * @author ezuykow
     */
    @Scheduled(fixedRate = MIN_IN_MILLIS)
    public void addOneMinuteToTimeSinceStart() {
        List<GlobalChat> chats = globalChatService.findAll().stream()
                .filter(GlobalChat::isGameStarted).toList();
        chats.forEach(c -> c.setMinutesSinceStart(c.getMinutesSinceStart() + 1));
        globalChatService.saveAll(chats);
        gameValidator.validateGamesTimeEnded(chats);
    }

    //-----------------API END-----------------

}

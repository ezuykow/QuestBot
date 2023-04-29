package ru.coffeecoders.questbot.scheduling;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.services.GlobalChatService;

import java.util.List;

/**
 * @author ezuykow
 */
@Component
@EnableScheduling
public class GamesTimer {

    private static final int MIN_IN_MILLIS = 60_000;

    private final GlobalChatService globalChatService;

    public GamesTimer(GlobalChatService globalChatService) {
        this.globalChatService = globalChatService;
    }

    //-----------------API START-----------------

    @Scheduled(fixedRate = MIN_IN_MILLIS)
    public void addOneMinuteToTimeSinceStart() {
        List<GlobalChat> chats = globalChatService.findAll().stream()
                .filter(GlobalChat::isGameStarted).toList();
        chats.forEach(c -> c.setMinutesSinceStart(c.getMinutesSinceStart() + 1));
        globalChatService.saveAll(chats);
    }

    //-----------------API END-----------------

}

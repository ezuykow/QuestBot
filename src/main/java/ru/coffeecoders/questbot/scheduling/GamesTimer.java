package ru.coffeecoders.questbot.scheduling;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.exceptions.NonExistentGame;
import ru.coffeecoders.questbot.messages.MessageBuilder;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.services.GameService;
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
    private final GameService gameService;
    private final GameValidator gameValidator;
    private final MessageSender messageSender;
    private final Messages messages;
    private final MessageBuilder messageBuilder;

    public GamesTimer(GlobalChatService globalChatService, GameService gameService, GameValidator gameValidator,
                      MessageSender messageSender, Messages messages, MessageBuilder messageBuilder) {
        this.globalChatService = globalChatService;
        this.gameService = gameService;
        this.gameValidator = gameValidator;
        this.messageSender = messageSender;
        this.messages = messages;
        this.messageBuilder = messageBuilder;
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

        chats.forEach(c -> {
            int minsSinceStart = c.getMinutesSinceStart() + 1;
            int maxMins = gameService.findByName(c.getCreatingGameName())
                    .orElseThrow(NonExistentGame::new).getMaxTimeMinutes();
            int minsToEnd = maxMins - minsSinceStart;

            c.setMinutesSinceStart(minsSinceStart);
            globalChatService.save(c);

            if (!gameValidator.validateGamesTimeEnded(c, minsToEnd)) {
                if (minsToEnd > 30) {
                    if (minsToEnd % 20 == 0) {
                        messageSender.send(c.getTgChatId(),
                                messageBuilder.build(messages.time(), c.getTgChatId()));
                    }
                } else {
                    if (minsToEnd % 5 == 0) {
                        messageSender.send(c.getTgChatId(),
                                messageBuilder.build(messages.time(), c.getTgChatId()));
                    }
                }
            }
        });
    }

    //-----------------API END-----------------

}

package ru.coffeecoders.questbot.validators;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.services.GlobalChatService;

/**
 * @author ezuykow
 */
@Component
public class GameValidator {

    private final GlobalChatService globalChatService;

    public GameValidator(GlobalChatService globalChatService) {
        this.globalChatService = globalChatService;
    }

    /**
     *
     * @return
     * @author ezuykow
     */
    public boolean isGameStarted(long chatId) {
        return globalChatService.findById(chatId)
                .filter(this::isGameStarted)
                .isPresent();
    }

    /**
     *
     * @return
     * @author ezuykow
     */
    public boolean isGameCreating(long chatId) {
        return globalChatService.findById(chatId)
                .filter(this::isGameCreating)
                .isPresent();
    }

    /**
     * @author ezuykow
     */
    private boolean isGameStarted(GlobalChat globalChat) {
        return globalChat.isGameStarted();
    }

    /**
     * @author ezuykow
     */
    private boolean isGameCreating(GlobalChat globalChat) {
        return globalChat.getCreatingGameName() != null
                && !isGameStarted(globalChat);
    }
}

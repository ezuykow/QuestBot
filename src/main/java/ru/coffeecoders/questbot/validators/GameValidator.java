package ru.coffeecoders.questbot.validators;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.services.GameService;
import ru.coffeecoders.questbot.services.GlobalChatService;
import ru.coffeecoders.questbot.services.NewGameCreatingStateService;

/**
 * @author ezuykow
 */
@Component
public class GameValidator {

    private final GlobalChatService globalChatService;
    private final NewGameCreatingStateService newGameCreatingStateService;
    private final GameService gameService;

    public GameValidator(GlobalChatService globalChatService, NewGameCreatingStateService newGameCreatingStateService, GameService gameService) {
        this.globalChatService = globalChatService;
        this.newGameCreatingStateService = newGameCreatingStateService;
        this.gameService = gameService;
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

    public boolean isNewGameCreating(long chatId) {
        return newGameCreatingStateService.findById(chatId).isPresent();
    }

    public boolean isGameNameAlreadyTaken(String gameName) {
        return gameService.findAll()
                .stream().anyMatch(g -> g.getGameName().equals(gameName))
                || newGameCreatingStateService.findAll()
                .stream().anyMatch(cg -> cg.getGameName() != null && cg.getGameName().equals(gameName));
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

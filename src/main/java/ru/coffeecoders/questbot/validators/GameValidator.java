package ru.coffeecoders.questbot.validators;

import org.springframework.stereotype.Component;
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

    //-----------------API START-----------------

    /**
     * @param chatId id чата
     * @return {@code true}, если в этом чате запущена игра, иначе {@code false}
     * @author ezuykow
     */
    public boolean isGameStarted(long chatId) {
        return globalChatService.findById(chatId)
                .filter(this::isGameStarted)
                .isPresent();
    }

    /**
     * @param chatId id чата
     * @return {@code true}, если в этом чате запущена подготовка к игре, иначе {@code false}
     * @author ezuykow
     */
    public boolean isGameCreating(long chatId) {
        return globalChatService.findById(chatId)
                .filter(this::isGameCreating)
                .isPresent();
    }

    /**
     * @param gameName название игры
     * @return {@code true}, если игра с таким названием подготавливается или запущена в любом чате,
     * иначе {@code false}
     * @author ezuykow
     */
    public boolean isGameCreating(String gameName) {
        return globalChatService.findAll().stream()
                .anyMatch(gc -> gc.getCreatingGameName().equals(gameName));
    }

    /**
     * @param chatId id чата
     * @return {@code true}, если в этом чате создается новая игра, иначе {@code false}
     * @author ezuykow
     */
    public boolean isNewGameCreating(long chatId) {
        return newGameCreatingStateService.findById(chatId).isPresent();
    }

    /**
     * @param gameName название игры
     * @return {@code true}, если игра с таким названием уже существует или создается, иначе {@code false}
     * @author ezuykow
     */
    public boolean isGameNameAlreadyTaken(String gameName) {
        return gameService.findAll()
                .stream().anyMatch(g -> g.getGameName().equals(gameName))
                || newGameCreatingStateService.findAll()
                .stream().anyMatch(cg -> cg.getGameName() != null && cg.getGameName().equals(gameName));
    }

    //-----------------API END-----------------

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

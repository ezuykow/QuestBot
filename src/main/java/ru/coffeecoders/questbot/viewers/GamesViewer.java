package ru.coffeecoders.questbot.viewers;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.models.GamesViewPage;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.services.GameService;

import java.util.List;

/**
 * @author ezuykow
 */
@Component
public class GamesViewer {

    private final GameService gameService;
    private final MessageSender msgSender;
    private final Environment env;

    public GamesViewer(GameService gameService, MessageSender msgSender, Environment env) {
        this.gameService = gameService;
        this.msgSender = msgSender;
        this.env = env;
    }

    public void viewGames(long chatId) {
        List<Game> games = gameService.findAll();
        validateAndShowGamesList(chatId, games);
    }

    private void validateAndShowGamesList(long chatId, List<Game> games) {
        if (!games.isEmpty()) {
            showGames(chatId, games);
        } else {
            msgSender.send(chatId, env.getProperty("messages.game.emptyGamesList"));
        }
    }

    private void showGames(long chatId, List<Game> games) {
        GamesViewPage page = GamesViewPage.createPage(games);
        msgSender.send(chatId, page.getText(), page.getKeyboard());
    }
}

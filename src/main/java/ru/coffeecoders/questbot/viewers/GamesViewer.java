package ru.coffeecoders.questbot.viewers;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.actions.newgame.utils.NewGameUtils;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.exceptions.NonExistentGame;
import ru.coffeecoders.questbot.managers.BlockingManager;
import ru.coffeecoders.questbot.managers.RestrictingManager;
import ru.coffeecoders.questbot.models.GameInfoPage;
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
    private final BlockingManager blockingManager;
    private final RestrictingManager restrictingManager;
    private final NewGameUtils utils;
    private final MessageSender msgSender;
    private final Environment env;

    //-----------------API START-----------------

    public GamesViewer(GameService gameService, BlockingManager blockingManager, RestrictingManager restrictingManager,
                       NewGameUtils utils, MessageSender msgSender, Environment env) {
        this.gameService = gameService;
        this.blockingManager = blockingManager;
        this.restrictingManager = restrictingManager;
        this.utils = utils;
        this.msgSender = msgSender;
        this.env = env;
    }

    public void viewGames(long chatId) {
        List<Game> games = gameService.findAll();
        validateAndShowGamesList(chatId, games);
    }

    public void showGame(long chatId, int msgId, String data) {
        String gameName = data.substring(data.lastIndexOf(".") + 1);
        Game game = gameService.findByName(gameName).orElseThrow(NonExistentGame::new);
        GameInfoPage page = GameInfoPage.createPage(game, env.getProperty("messages.game.gameInfo"), utils);
        msgSender.edit(chatId, msgId, page.getText(), page.getKeyboard());
    }

    public void closeView(long chatId, int msgId) {
        unBlockAndUnrestrictChat(chatId);
        msgSender.sendDelete(chatId, msgId);
    }

    //-----------------API END-----------------

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

    private void unBlockAndUnrestrictChat(long chatId) {
        restrictingManager.unRestrictMembers(chatId);
        blockingManager.unblockAdminChat(chatId, env.getProperty("messages.admins.endGamesView"));
    }
}

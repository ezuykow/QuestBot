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
import ru.coffeecoders.questbot.validators.GameValidator;

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
    private final GameValidator validator;
    private final MessageSender msgSender;
    private final Environment env;

    //-----------------API START-----------------

    public GamesViewer(GameService gameService, BlockingManager blockingManager, RestrictingManager restrictingManager,
                       NewGameUtils utils, GameValidator validator, MessageSender msgSender, Environment env) {
        this.gameService = gameService;
        this.blockingManager = blockingManager;
        this.restrictingManager = restrictingManager;
        this.utils = utils;
        this.validator = validator;
        this.msgSender = msgSender;
        this.env = env;
    }

    public void viewGames(long chatId) {
        validateAndShowGamesList(chatId, -1);
    }

    public void showGame(long chatId, int msgId, String data) {
        String gameName = data.substring(data.lastIndexOf(".") + 1);
        Game game = gameService.findByName(gameName).orElseThrow(NonExistentGame::new);
        GameInfoPage page = GameInfoPage.createPage(game, env.getProperty("messages.game.gameInfo"), utils);
        msgSender.edit(chatId, msgId, page.getText(), page.getKeyboard());
    }

    public void deleteGame(String callbackId, long chatId, int msgId, String data) {
        String gameName = data.substring(data.lastIndexOf(".") + 1);
        if (validator.isGameCreating(gameName)) {
            msgSender.sentToast(callbackId, env.getProperty("messages.game.failedDeletingGame"), true);
        } else {
            gameService.deleteByGameName(gameName);
            msgSender.sendDelete(chatId, msgId);
            viewGames(chatId);
        }
    }

    public void backFromInfo(long chatId, int msgId) {
        editGameMsg(chatId, msgId);
    }

    public void closeView(long chatId, int msgId) {
        unBlockAndUnrestrictChat(chatId);
        msgSender.sendDelete(chatId, msgId);
    }

    //-----------------API END-----------------

    private void editGameMsg(long chatId, int msgId) {
        validateAndShowGamesList(chatId, msgId);
    }

    private void validateAndShowGamesList(long chatId, int msgId) {
        List<Game> games = gameService.findAll();
        if (!games.isEmpty()) {
            showGames(chatId, games, msgId);
        } else {
            msgSender.send(chatId, env.getProperty("messages.game.emptyGamesList"));
            unBlockAndUnrestrictChat(chatId);
        }
    }

    private void showGames(long chatId, List<Game> games, int msgId) {
        GamesViewPage page = GamesViewPage.createPage(games);
        if (msgId == -1) {
            msgSender.send(chatId, page.getText(), page.getKeyboard());
        } else {
            msgSender.edit(chatId, msgId, page.getText(), page.getKeyboard());
        }
    }

    private void unBlockAndUnrestrictChat(long chatId) {
        restrictingManager.unRestrictMembers(chatId);
        blockingManager.unblockAdminChat(chatId, env.getProperty("messages.admins.endGamesView"));
    }
}

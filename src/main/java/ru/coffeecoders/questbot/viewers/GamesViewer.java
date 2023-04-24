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

    //-----------------API START-----------------

    /**
     * Вызывает {@link GamesViewer#validateAndShowGamesList} с {@code chatId} и {@code msgId = -1}
     * @param chatId id чата
     * @author ezuykow
     */
    public void viewGames(long chatId) {
        validateAndShowGamesList(chatId, -1);
    }

    /**
     * Достает из {@code data} название игры, находит игру по названию в БД, запрашивает для нее
     * {@link GameInfoPage} и отправляет данные полученной страницы в чат
     * @param chatId id чата
     * @param msgId id сообщения, где отображен список игр
     * @param data данные калбака
     * @author ezuykow
     */
    public void showGame(long chatId, int msgId, String data) {
        String gameName = data.substring(data.lastIndexOf(".") + 1);
        Game game = gameService.findByName(gameName).orElseThrow(NonExistentGame::new);
        GameInfoPage page = GameInfoPage.createPage(game, env.getProperty("messages.game.gameInfo"), utils);
        msgSender.edit(chatId, msgId, page.getText(), page.getKeyboard());
    }

    /**
     * Достает из {@code data} название игры и, если эта игра нигде не запущена, то удаляет ее, иначе отправляет
     * {@link MessageSender#sentToast}, что эту игру удалить нельзя
     * @param callbackId id калбака, в котором пришли данные на удаление игры
     * @param chatId id чата
     * @param msgId id сообщения, где отображалась игра
     * @param data данные калбака
     * @author ezuykow
     */
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

    /**
     * Вызывает {@link GamesViewer#editGameMsg} с {@code chatId} и {@code msgId}
     * @param chatId id чата
     * @param msgId id сообщения, где отображались данные игры
     * @author ezuykow
     */
    public void backFromInfo(long chatId, int msgId) {
        editGameMsg(chatId, msgId);
    }

    /**
     * Вызывает {@link GamesViewer#unBlockAndUnrestrictChat} и удаляет из чата сообщение с {@code id = msgId}
     * @param chatId id чата
     * @param msgId id сообщения, где отображался список игр
     * @author ezuykow
     */
    public void closeView(long chatId, int msgId) {
        unBlockAndUnrestrictChat(chatId);
        msgSender.sendDelete(chatId, msgId);
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void editGameMsg(long chatId, int msgId) {
        validateAndShowGamesList(chatId, msgId);
    }

    /**
     * @author ezuykow
     */
    private void validateAndShowGamesList(long chatId, int msgId) {
        List<Game> games = gameService.findAll();
        if (!games.isEmpty()) {
            showGames(chatId, games, msgId);
        } else {
            msgSender.send(chatId, env.getProperty("messages.game.emptyGamesList"));
            unBlockAndUnrestrictChat(chatId);
        }
    }

    /**
     * @author ezuykow
     */
    private void showGames(long chatId, List<Game> games, int msgId) {
        GamesViewPage page = GamesViewPage.createPage(games);
        if (msgId == -1) {
            msgSender.send(chatId, page.getText(), page.getKeyboard());
        } else {
            msgSender.edit(chatId, msgId, page.getText(), page.getKeyboard());
        }
    }

    /**
     * @author ezuykow
     */
    private void unBlockAndUnrestrictChat(long chatId) {
        restrictingManager.unRestrictMembers(chatId);
        blockingManager.unblockAdminChat(chatId, env.getProperty("messages.admins.endGamesView"));
    }
}

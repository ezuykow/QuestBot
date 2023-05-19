package ru.coffeecoders.questbot.viewers;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.exceptions.NonExistentChat;
import ru.coffeecoders.questbot.keyboards.PrepareGameRequestKeyboard;
import ru.coffeecoders.questbot.managers.TaskCreationManager;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.services.GameService;
import ru.coffeecoders.questbot.services.GlobalChatService;

import java.util.List;

/**
 * @author ezuykow
 */
@Component
public class PrepareGameViewer {

    private final TaskCreationManager gameManager;
    private final GameService gameService;
    private final GlobalChatService globalChatService;
    private final MessageSender msgSender;
    private final Messages messages;

    public PrepareGameViewer(TaskCreationManager gameManager, GameService gameService, GlobalChatService globalChatService,
                             MessageSender msgSender, Messages messages) {
        this.gameManager = gameManager;
        this.gameService = gameService;
        this.globalChatService = globalChatService;
        this.msgSender = msgSender;
        this.messages = messages;
    }

    //-----------------API START-----------------

    /**
     * Отправляет в чат запрос на выбор названия игры, которую нужно подготовить
     * @param senderAdminId id инициировавшего админа
     * @param chatId id чата
     * @author ezuykow
     */
    public void requestGameName(long senderAdminId, String adminUsername, long chatId) {
        final List<String> gamesNames = gameService.findAll().stream().map(Game::getGameName).toList();
        validateListAndSendMsg(gamesNames, senderAdminId, adminUsername, chatId);
    }

    public void startPrepare(long chatId, Game game) {
        GlobalChat chat = globalChatService.findById(chatId).orElseThrow(NonExistentChat::new);
        chat.setCreatingGameName(game.getGameName());
        globalChatService.save(chat);
        msgSender.send(chatId,
                String.format(messages.prepareGameStartedHint(), game.getGameName()));
        gameManager.createTasks(chatId, game);
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void validateListAndSendMsg(List<String> gamesNames, long senderAdminId, String adminUsername, long chatId) {
        if (!gamesNames.isEmpty()) {
            final String text = adminUsername + messages.choosePreparingGame();
            final InlineKeyboardMarkup keyboard = PrepareGameRequestKeyboard.createKeyboard(gamesNames, senderAdminId);
            msgSender.send(chatId, text, keyboard);
        } else {
            msgSender.send(chatId, messages.emptyGamesList() + ", " + adminUsername);
        }
    }
}

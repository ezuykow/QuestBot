package ru.coffeecoders.questbot.viewers;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.exceptions.NonExistentChat;
import ru.coffeecoders.questbot.keyboards.PrepareGameRequestKeyboard;
import ru.coffeecoders.questbot.managers.TaskCreationManager;
import ru.coffeecoders.questbot.messages.MessageBuilder;
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
    private final MessageBuilder messageBuilder;

    public PrepareGameViewer(TaskCreationManager gameManager, GameService gameService, GlobalChatService globalChatService,
                             MessageSender msgSender, Messages messages, MessageBuilder messageBuilder) {
        this.gameManager = gameManager;
        this.gameService = gameService;
        this.globalChatService = globalChatService;
        this.msgSender = msgSender;
        this.messages = messages;
        this.messageBuilder = messageBuilder;
    }

    //-----------------API START-----------------

    /**
     * Отправляет в чат запрос на выбор названия игры, которую нужно подготовить
     * @param senderAdminId id инициировавшего админа
     * @param chatId id чата
     * @author ezuykow
     */
    public void requestGameName(long senderAdminId, String adminUsername, long chatId) {
        final List<Game> games = gameService.findAll();
        validateListAndSendMsg(games, senderAdminId, adminUsername, chatId);
    }

    public void startPrepare(long chatId, Game game) {
        GlobalChat chat = globalChatService.findById(chatId).orElseThrow(NonExistentChat::new);
        chat.setCreatingGameName(game.getGameName());
        globalChatService.save(chat);
        msgSender.send(chatId, messageBuilder.build(messages.prepareGameStartedHint(), game, chat));
        gameManager.createTasks(chatId, game);
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void validateListAndSendMsg(List<Game> games, long senderAdminId, String adminUsername, long chatId) {
        if (!games.isEmpty()) {
            final InlineKeyboardMarkup keyboard = PrepareGameRequestKeyboard.createKeyboard(games, senderAdminId);
            msgSender.send(chatId,
                    messageBuilder.build(adminUsername + messages.choosePreparingGame(), chatId), keyboard);
        } else {
            msgSender.send(chatId, messages.emptyGamesList() + ", " + adminUsername);
        }
    }
}

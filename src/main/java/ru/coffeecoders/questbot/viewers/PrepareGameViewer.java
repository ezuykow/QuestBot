package ru.coffeecoders.questbot.viewers;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.keyboards.PrepareGameRequestKeyboard;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.services.GameService;

import java.util.List;

/**
 * @author ezuykow
 */
@Component
public class PrepareGameViewer {

    private final GameService gameService;
    private final MessageSender msgSender;
    private final Messages messages;

    public PrepareGameViewer(GameService gameService, MessageSender msgSender, Messages messages) {
        this.gameService = gameService;
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

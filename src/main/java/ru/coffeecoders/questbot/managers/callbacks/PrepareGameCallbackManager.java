package ru.coffeecoders.questbot.managers.callbacks;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.exceptions.NonExistentGame;
import ru.coffeecoders.questbot.messages.MessageBuilder;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.services.GameService;
import ru.coffeecoders.questbot.validators.GameValidator;
import ru.coffeecoders.questbot.viewers.PrepareGameViewer;

/**
 * @author ezuykow
 */
@Component
public class PrepareGameCallbackManager {

    private final PrepareGameViewer prepareGameViewer;
    private final GameService gameService;
    private final GameValidator validator;
    private final MessageSender msgSender;
    private final Messages messages;
    private final MessageBuilder messageBuilder;

    public PrepareGameCallbackManager(PrepareGameViewer prepareGameViewer, GameService gameService,
                                      GameValidator validator, MessageSender msgSender,
                                      Messages messages, MessageBuilder messageBuilder) {
        this.prepareGameViewer = prepareGameViewer;
        this.gameService = gameService;
        this.validator = validator;
        this.msgSender = msgSender;
        this.messages = messages;
        this.messageBuilder = messageBuilder;
    }

    //-----------------API START-----------------

    /**
     * Если кнопку нажал инициатор, то если нажата кнопка "Отмена" - то удаляет сообщение, иначе вызывает
     * {@link PrepareGameCallbackManager#performCallback}
     * @param senderUserId id пользователя, нажавшего кнопку
     * @param chatId id чата
     * @param msgId id сообщения
     * @param data данные калбака
     * @author ezuykow
     */
    public void manageCallback(String callbackId, long senderUserId, long chatId, int msgId, String data) {
        long initiatorId = Long.parseLong(data.substring(data.lastIndexOf(".") + 1));
        if (senderUserId == initiatorId) {
            if (data.matches("PrepareGame.Close.*")) {
                msgSender.sendDelete(chatId, msgId);
            } else {
                performCallback(callbackId, chatId, msgId, data);
            }
        }
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void performCallback(String callbackId, long chatId, int msgId, String data) {
        int gameId = Integer.parseInt(data.substring(data.indexOf(".") + 1, data.lastIndexOf(".")));
        Game game = gameService.findById(gameId).orElseThrow(NonExistentGame::new);
        if (validator.isRightQuestionsCount(game)) {
            msgSender.sendDelete(chatId, msgId);
            prepareGameViewer.startPrepare(chatId, game);
        } else {
            msgSender.sendToast(callbackId,
                    messageBuilder.build(messages.notEnoughQuestions(), chatId), true);
        }
    }
}

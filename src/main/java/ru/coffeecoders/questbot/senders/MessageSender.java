package ru.coffeecoders.questbot.senders;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.MessageToDelete;
import ru.coffeecoders.questbot.services.MessageToDeleteService;

import java.util.List;

/**
 * @author ezuykow
 */
@Component
public class MessageSender {

    private final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    private final TelegramBot bot;
    private final MessageToDeleteService messageToDeleteService;

    public MessageSender(TelegramBot bot, MessageToDeleteService messageToDeleteService) {
        this.bot = bot;
        this.messageToDeleteService = messageToDeleteService;
    }

    /**
     * Отправляет сообщение с текстом {@code text} в чат c id {@code chatId}
     * @param chatId id чата, в который отправить сообщение
     * @param text текст сообщения
     */
    public void send(long chatId, String text) {
        checkResponse(bot.execute(
                new SendMessage(chatId, text)));
    }

    /**
     * Отправляет сообщение с текстом {@code text} в чат c id {@code chatId},
     * а также клавиатуру {@code kb}
     * @param chatId id чата, в который отправить сообщение
     * @param text текст сообщения
     * @param kb клавиатура, которую нужно отправить с сообщением
     */
    public void send(long chatId, String text, Keyboard kb) {
        checkResponse(bot.execute(
                new SendMessage(chatId, text).replyMarkup(kb)));
    }

    /**
     * Отправляет сообщение с текстом {@code text} в чат c id {@code chatId},
     * а также клавиатуру {@code kb} ответом на сообщение с id {@code replyToMessageId}
     * @param chatId id чата, в который отправить сообщение
     * @param text текст сообщения
     * @param kb клавиатура, которую нужно отправить с сообщением
     * @param replyToMessageId id сообщения, на которое отвечаем
     */
    public void send(long chatId, String text, Keyboard kb, int replyToMessageId) {
        checkResponse(bot.execute(
                new SendMessage(chatId, text).replyMarkup(kb).replyToMessageId(replyToMessageId)));
    }

    /**
     * Отправляет сообщение с текстом {@code text} в чат c id {@code chatId}
     * ответом на сообщение с id {@code replyToMessageId}
     * @param chatId id чата, в который отправить сообщение
     * @param text текст сообщения
     * @param replyToMessageId id сообщения, на которое отвечаем
     * @return {@link SendResponse} ответ сервера на отправленное сообщение
     */
    public SendResponse send(long chatId, String text, int replyToMessageId) {
        SendResponse response = bot.execute(
                new SendMessage(chatId, text).replyMarkup(new ForceReply(true))
                        .replyToMessageId(replyToMessageId));
        checkResponse(response);
        return response;
    }

    /**
     * Редактирует сообщение с id {@code msgId} в чате с id {@code chatId} - меняет текст на
     * {@code text}, клавиатуру на {@code kb}, или приклепляет клавиатуру, если ее не было
     * @param chatId id чата
     * @param msgId id сообщения
     * @param text новый текст
     * @param kb клавиатура
     */
    public void edit(long chatId, int msgId, String text, InlineKeyboardMarkup kb) {
        checkResponse(bot.execute(
                new EditMessageText(chatId, msgId, text).replyMarkup(kb)));
    }

    /**
     * Удаляет сообщение с id {@code msgId} в чате с id {@code chatId}
     * @param chatId id чата
     * @param msgId id сообщения
     */
    public void sendDelete(long chatId, int msgId) {
        checkResponse(bot.execute(
                new DeleteMessage(chatId, msgId)));
    }

    /**
     * Вызывает {@link MessageSender#sendDelete} для всех сообщений,
     * находящихся в таблице msg_to_delete в БД с полем active = false
     */
    public void sendDeleteAllMessageToDelete() {
        List<MessageToDelete> mtds = messageToDeleteService.findAll().stream().
                filter(m -> !m.isActive())
                .toList();
        mtds.forEach(m -> sendDelete(m.getChatId(), m.getMsgId()));
        messageToDeleteService.deleteAll(mtds);
    }

    private void checkResponse(BaseResponse response) {
        if (!response.isOk()) {
            logger.warn("Unsent msg! Error code: {}", response.errorCode());
        }
    }
}

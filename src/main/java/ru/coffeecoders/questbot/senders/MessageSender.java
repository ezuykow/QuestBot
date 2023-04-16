package ru.coffeecoders.questbot.senders;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author ezuykow
 */
@Component
public class MessageSender {

    private final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    private final TelegramBot bot;

    public MessageSender(TelegramBot bot) {
        this.bot = bot;
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
    public void delete(long chatId, int msgId) {
        checkResponse(bot.execute(
                new DeleteMessage(chatId, msgId)));
    }

    private void checkResponse(BaseResponse response) {
        if (!response.isOk()) {
            logger.warn("Unsent msg! Error code: {}", response.errorCode());
        }
    }
}

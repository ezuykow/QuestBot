package ru.coffeecoders.questbot.senders;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
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

    public void send(long chatId, String text) {
        checkResponse(bot.execute(
                new SendMessage(chatId, text)));
    }

    public void send(long chatId, String text, Keyboard kb) {
        checkResponse(bot.execute(
                new SendMessage(chatId, text).replyMarkup(kb)));
    }

    public void edit(long chatId, int msgId, String text, InlineKeyboardMarkup kb) {
        checkResponse(bot.execute(
                new EditMessageText(chatId, msgId, text).replyMarkup(kb)));
    }

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

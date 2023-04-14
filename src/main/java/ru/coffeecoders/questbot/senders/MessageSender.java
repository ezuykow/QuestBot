package ru.coffeecoders.questbot.senders;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.SendMessage;
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
        exec(new SendMessage(chatId, text));
    }

    public void send(long chatId, String text, Keyboard kb) {
        exec(new SendMessage(chatId, text).replyMarkup(kb));
    }

    public void edit(long chatId, long msgId, String text, InlineKeyboardMarkup kb) {
        //TODO
    }

    private void exec(SendMessage msg) {
        SendResponse response = bot.execute(msg);
        if (!response.isOk()) {
            logger.warn("Unsent msg! {}", response.message().text());
        }
    }
}

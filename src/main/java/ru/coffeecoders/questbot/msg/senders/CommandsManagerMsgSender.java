package ru.coffeecoders.questbot.msg.senders;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class CommandsManagerMsgSender {

    private final Logger logger = LoggerFactory.getLogger(CommandsManagerMsgSender.class);

    private final Environment env;

    private final TelegramBot telegramBot;

    public CommandsManagerMsgSender(Environment env, TelegramBot telegramBot) {
        this.env = env;
        this.telegramBot = telegramBot;
    }

    public void sendInvalidCmdMsg(long chatId){
        checkResponse(telegramBot.execute(createSendMessage(chatId,  env.getProperty("messages.admins.invalidMsg"))));
    }

    public void sendNotAdminMsg(long chatId){
        checkResponse(telegramBot.execute(createSendMessage(chatId,  env.getProperty("messages.admins.cmdSendByNotAdmin"))));
    }
    public void sendAdminCmdInGlobalChatMsg(long chatId){
        checkResponse(telegramBot.execute(createSendMessage(chatId,  env.getProperty("messages.admins.adminCmdInGlobalChat"))));
    }

    public void sendGameCmdInAdminChatMsg(long chatId){
        checkResponse(telegramBot.execute(createSendMessage(chatId,  env.getProperty("messages.admins.gameCmdInAdminChat"))));
    }

    private SendMessage createSendMessage(long chatId, String msg) {
        return new SendMessage(chatId,  msg);
    }

    private void checkResponse(SendResponse response) {
        if (!response.isOk()) {
            logger.warn("Unsent msg! {}", response.message().text());
        }
    }
}

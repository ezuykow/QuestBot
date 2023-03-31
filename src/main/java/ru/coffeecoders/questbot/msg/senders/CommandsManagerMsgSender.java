package ru.coffeecoders.questbot.msg.senders;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.core.env.Environment;

public class CommandsManagerMsgSender {

    private final Environment env;

    private final TelegramBot telegramBot;

    public CommandsManagerMsgSender(Environment env, TelegramBot telegramBot) {
        this.env = env;
        this.telegramBot = telegramBot;
    }

    public void sendInvalidMsg(long chatId){
        SendMessage message = new SendMessage(chatId, env.getProperty("messages.admins.invalidMessage"));
        telegramBot.execute(message);
    }

    public void sendNonAdminMsg(long chatId){
        SendMessage message = new SendMessage(chatId,  env.getProperty("messages.admins.commandSentByNonAdmin"));
        telegramBot.execute(message);
    }
    public void sendAdminsMsgAdminCmdGameChat(long chatId){
        SendMessage message = new SendMessage(chatId, env.getProperty("messages.admins.sendAdminsMsgAdminCmdGameChat"));
        telegramBot.execute(message);
    }

    public void sendGamesMsgAdminChat(long chatId){
        SendMessage message = new SendMessage(chatId, env.getProperty("messages.admins.sendGamesMsgAdminChat"));
        telegramBot.execute(message);
    }

}

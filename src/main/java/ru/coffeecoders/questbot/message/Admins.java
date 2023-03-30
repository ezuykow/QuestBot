package ru.coffeecoders.questbot.message;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;

import java.util.List;

public class Admins {

    private Environment env;

    TelegramBot telegramBot;

    public void sendInvalidMsg(long chatId){
        SendMessage message = new SendMessage(chatId, env.getProperties().getProperty("messages.admins.invalidMessage"));
        telegramBot.execute(message);
    }

    public void sendNonAdminMsg(long chatId){
            SendMessage message = new SendMessage(chatId,  env.getProperties().getProperty("messages.admins.commandSentByNonAdmin"));
            telegramBot.execute(message);
    }
    public void sendAdminsMsgAdminCmdGameChat(long chatId){
            SendMessage message = new SendMessage(chatId, env.getProperties().getProperty("message.admins.sendAdminsMsgAdminCmdGameChat"));
            telegramBot.execute(message);
    }

    public void sendGamesMsgAdminChat(long chatId){
            SendMessage message = new SendMessage(chatId, env.getProperties().getProperty("message.admins.sendGamesMsgAdminChat"));
            telegramBot.execute(message);
    }

}




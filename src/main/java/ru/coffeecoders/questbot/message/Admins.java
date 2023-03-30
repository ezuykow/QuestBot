package ru.coffeecoders.questbot.message;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;

import java.util.List;

public class Admins {

    @Value("@{messages.admins.invalidMessage}")
    private String invalidCommand;

    @Value("@{messages.admins.commandSentByNonAdmin}")
    private String commandNonAdmin;

    @Value("@{message.admins.sendAdminsMsgAdminCmdGameChat}")
    private String adminsMsgAdminCmdGameChat;

    @Value("@{message.admins.sendGamesMsgAdminChat}")
    private String gamesMsgAdminChat;

    TelegramBot telegramBot;

    public void sendInvalidMsg(List<Update> updates){
        updates.forEach(update -> {
            SendMessage message = new SendMessage(update.message().text(), invalidCommand);
            telegramBot.execute(message);
        });
    }

    public void sendNonAdminMsg(List<Update> updates){
        updates.forEach(update -> {
            SendMessage message = new SendMessage(update.message().text(),  commandNonAdmin);
            telegramBot.execute(message);
        });
    }
    public void sendAdminsMsgAdminCmdGameChat(List<Update> updates){
        updates.forEach(update -> {
            SendMessage message = new SendMessage(update.message().text(), adminsMsgAdminCmdGameChat);
            telegramBot.execute(message);
        });
    }

    public void sendGamesMsgAdminChat(List<Update> updates){
        updates.forEach(update -> {
            SendMessage message = new SendMessage(update.message().text(), gamesMsgAdminChat);
            telegramBot.execute(message);
        });
    }

}




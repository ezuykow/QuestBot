package ru.coffeecoders.questbot.commands.admins.keybords.keyboards.creators;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;


public class AllQuestionsKeyboard {


    public void allRunningGamesCommand(Update update) {

            String replyText = msgSender.getAllGames();
            SendMessage request = new SendMessage(update.message().chat().id(), replyText);
            SendResponse response = msgSender.poll(request);

    }


}
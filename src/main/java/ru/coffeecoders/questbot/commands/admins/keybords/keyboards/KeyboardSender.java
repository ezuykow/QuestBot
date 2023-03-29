package ru.coffeecoders.questbot.commands.admins.keybords.keyboards;


import ru.coffeecoders.questbot.commands.admins.keybords.keyboards.creators.Keyboard;

import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class KeyboardSender implements Keyboard {

   //TODO ещё думаю как реализовать

    public void sendKeyboard (Keyboard keyboard){


        SendMessage request = new SendMessage(chatId, replyText).replyMarkup(keyboardMarkup);
        msgSender.poll(request);
    }


}

package ru.coffeecoders.questbot.commands.admins.keybords.keyboards;


import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.commands.admins.keybords.keyboards.creators.Keyboard;

@Component


public class KeyboardSender implements Keyboard {

    //TODO ещё думаю как реализовать

    public void sendKeyboard(Keyboard keyboard, Update update) {
        messageSender.sendMessage(keyboard, update);
    }
}
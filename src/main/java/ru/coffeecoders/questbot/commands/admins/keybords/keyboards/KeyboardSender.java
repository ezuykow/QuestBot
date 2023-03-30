package ru.coffeecoders.questbot.commands.admins.keybords.keyboards;


import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.commands.admins.keybords.keyboards.creators.Keyboard;

@Component


public class KeyboardSender implements Keyboard {

    //TODO сборка для ответа в messageSender

    public void sendKeyboard(Keyboard keyboard, Update update) {
        messageSender.sendMessage(keyboard, update);
    }
}
package ru.coffeecoders.questbot.keyboards;


import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import org.springframework.stereotype.Component;

@Component


public class KeyboardSender {

    //TODO сборка для ответа в messageSender

    public void sendKeyboard(Keyboard keyboard, Update update) {
        msgSender.sendMessage(keyboard, update);
    }
    public void changeKeyboard(Keyboard keyboard, Update update) {
        msgSender.sendMessage(keyboard, update);
    }


}
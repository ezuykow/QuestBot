package ru.coffeecoders.questbot.keyboards;


import com.pengrad.telegrambot.model.request.Keyboard;
import org.springframework.stereotype.Component;

@Component
public class KeyboardSender {

    //TODO сборка для ответа в messageSender

    public void sendKeyboard(Keyboard keyboard, Long chatId) {
        msgSender.sendMessage(keyboard, chatId);
    }

    //TODO тут или в месенджере
    public void sendTextAndKeyboard(Keyboard keyboard, String text, Long chatId) {
        msgSender.sendMessage(keyboard,text, chatId);
    }
}
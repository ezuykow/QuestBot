package ru.coffeecoders.questbot.managers;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UpdateManager {

    public void performUpdate(Update update) {
        Optional<String> textOpt = tryGetTextMessage(update.message());
        textOpt.ifPresent(this::checkTextForCommand);

    }

    private void checkTextForCommand(String text) {
        if (text.trim().matches("/.*")) {
            //TODO
        }
    }

    private Optional<String> tryGetTextMessage(Message message) {
        try {
            return Optional.of(message.text());
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }


}

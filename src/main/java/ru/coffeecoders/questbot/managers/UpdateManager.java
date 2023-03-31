package ru.coffeecoders.questbot.managers;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class UpdateManager {
//    private final CommandsManager commandsManager;

//    public UpdateManager(CommandsManager commandsManager) {
//        this.commandsManager = commandsManager;
//    }

    public void performUpdate(Update update) {
        Optional<String> textOpt = tryGetTextMessage(update.message());
        textOpt.ifPresent(text -> performIfCommand(text, update));

    }

    private void performIfCommand(String text, Update update) {
        if (text.trim().matches("/.*")) {
//            commandsManager.manageCommand(update);
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

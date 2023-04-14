package ru.coffeecoders.questbot.managers;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.models.ExtendedUpdate;

import java.util.Optional;

@Component
public class UpdateManager {

    private final CommandsManager commandsManager;

    public void performUpdate(Update update) {
        ExtendedUpdate exUpdate = new ExtendedUpdate(update);

        switch (exUpdate.getUpdateType()) {
            case COMMAND -> commandsManager.manageCommand(update);
            case DOCUMENT -> documentsManager.manageDocument(update);
            case UNKNOWN -> {} //TODO msgSender.unknownUpdateType(chatId);
        }
    }
}

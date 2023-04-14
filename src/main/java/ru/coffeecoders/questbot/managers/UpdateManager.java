package ru.coffeecoders.questbot.managers;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.managers.commands.CommandsManager;
import ru.coffeecoders.questbot.models.ExtendedUpdate;

@Component
public class UpdateManager {

    private final CommandsManager commandsManager;
    private final DocumentsManager documentsManager;

    public UpdateManager(CommandsManager commandsManager, DocumentsManager documentsManager) {
        this.commandsManager = commandsManager;
        this.documentsManager = documentsManager;
    }

    public void performUpdate(Update update) {
        ExtendedUpdate exUpdate = new ExtendedUpdate(update);

        switch (exUpdate.getUpdateType()) {
            case COMMAND -> commandsManager.manageCommand(update);
            case DOCUMENT -> documentsManager.manageDocument(update);
            case UNKNOWN -> {} //TODO msgSender.unknownUpdateType(chatId);
        }
    }
}

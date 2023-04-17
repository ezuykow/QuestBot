package ru.coffeecoders.questbot.managers;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.managers.commands.CommandsManager;
import ru.coffeecoders.questbot.models.ExtendedUpdate;

@Component
public class UpdateManager {

    private final CommandsManager commandsManager;
    private final DocumentsManager documentsManager;
    private final CallbackQueryManager callbackManager;
    private final SimpleMessageManager simpleMessageManager;

    public UpdateManager(CommandsManager commandsManager, DocumentsManager documentsManager, CallbackQueryManager callbackManager, SimpleMessageManager simpleMessageManager) {
        this.commandsManager = commandsManager;
        this.documentsManager = documentsManager;
        this.callbackManager = callbackManager;
        this.simpleMessageManager = simpleMessageManager;
    }

    public void performUpdate(Update update) {
        ExtendedUpdate exUpdate = new ExtendedUpdate(update);
        switch (exUpdate.getUpdateType()) {
            case COMMAND -> commandsManager.manageCommand(exUpdate);
            case DOCUMENT -> documentsManager.manageDocument(exUpdate);
            case CALLBACK -> callbackManager.manageCallback(exUpdate);
            case SIMPLE_MESSAGE -> simpleMessageManager.manageMessage(exUpdate);
            case UNKNOWN -> {} //Игнорировать апдейт
        }
    }
}

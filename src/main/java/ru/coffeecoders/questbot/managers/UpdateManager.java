package ru.coffeecoders.questbot.managers;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.managers.callbacks.CallbackQueryManager;
import ru.coffeecoders.questbot.managers.commands.CommandsManager;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.messages.MessageSender;

import static ru.coffeecoders.questbot.models.ExtendedUpdate.UpdateType.*;

@Component
public class UpdateManager {

    private final CommandsManager commandsManager;
    private final DocumentsManager documentsManager;
    private final CallbackQueryManager callbackManager;
    private final SimpleMessageManager simpleMessageManager;
    private final ChatMembersManager chatMembersManager;
    private final MessageSender msgSender;

    public UpdateManager(CommandsManager commandsManager, DocumentsManager documentsManager,
                         CallbackQueryManager callbackManager, SimpleMessageManager simpleMessageManager,
                         ChatMembersManager chatMembersManager, MessageSender msgSender)
    {
        this.commandsManager = commandsManager;
        this.documentsManager = documentsManager;
        this.callbackManager = callbackManager;
        this.simpleMessageManager = simpleMessageManager;
        this.chatMembersManager = chatMembersManager;
        this.msgSender = msgSender;
    }

    /**
     *
     * @author anatoliy
     * @Redact: ezuykow
     */
    public void performUpdate(Update update) {
        ExtendedUpdate exUpdate = new ExtendedUpdate(update);
        switch (exUpdate.getUpdateType()) {
            case COMMAND ->
                    commandsManager.manageCommand(exUpdate);
            case NEW_OR_LEFT_CHAT_MEMBERS_MESSAGE ->
                    msgSender.sendDelete(exUpdate.getMessageChatId(), exUpdate.getMessageId());
            case DOCUMENT ->
                    documentsManager.manageDocument(exUpdate);
            case CALLBACK ->
                    callbackManager.manageCallback(exUpdate);
            case NEW_CHAT_MEMBER ->
                    chatMembersManager.manageChatMembers(exUpdate, NEW_CHAT_MEMBER);
            case LEFT_CHAT_MEMBER ->
                    chatMembersManager.manageChatMembers(exUpdate, LEFT_CHAT_MEMBER);
            case SIMPLE_MESSAGE ->
                    simpleMessageManager.manageMessage(exUpdate);
            case UNKNOWN -> {} //Игнорировать апдейт
        }
    }

    /**
     * После сна бота обрабатываем только изменения статусов пользователей
     * @param update апдейт
     * @author ezuykow
     */
    public void performUpdateAfterSleep(Update update) {
        ExtendedUpdate exUpdate = new ExtendedUpdate(update);
        switch (exUpdate.getUpdateType()) {
            case NEW_CHAT_MEMBER ->
                    chatMembersManager.manageChatMembers(exUpdate, NEW_CHAT_MEMBER);
            case LEFT_CHAT_MEMBER ->
                    chatMembersManager.manageChatMembers(exUpdate, LEFT_CHAT_MEMBER);
        }
    }
}

package ru.coffeecoders.questbot.managers;

import com.pengrad.telegrambot.model.ChatMemberUpdated;
import com.pengrad.telegrambot.model.User;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.actions.ChatMembersActions;
import ru.coffeecoders.questbot.logs.LogSender;
import ru.coffeecoders.questbot.models.ExtendedUpdate;

/**
 * @author ezuykow
 */
@Component
public class ChatMembersManager {

    private final ChatMembersActions chatMembersActions;
    private final LogSender logger;

    public ChatMembersManager(ChatMembersActions actions, LogSender logger) {
        this.chatMembersActions = actions;
        this.logger = logger;
    }

    //-----------------API START-----------------

    /**
     * В зависимости от {@code type} вызывает метод из {@link ChatMembersActions}
     * @param update апдейт с {@link ChatMemberUpdated}
     * @param type тип апдейта
     * @author ezuykow
     */
    public void manageChatMembers(ExtendedUpdate update, ExtendedUpdate.UpdateType type) {
        logger.warn("Обрабатываю изменение статуса пользователя");
        long chatId = update.getUpdatedMemberChatId();
        User updatedMember = update.getUpdatedMemberUser();
        switch (type) {
            case NEW_CHAT_MEMBER ->
                chatMembersActions.newChatMember(updatedMember, chatId);
            case LEFT_CHAT_MEMBER ->
                chatMembersActions.leftChatMember(updatedMember, chatId);
        }
    }


    //-----------------API END-----------------

}

package ru.coffeecoders.questbot.managers;

import com.pengrad.telegrambot.model.User;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.actions.ChatMembersActions;
import ru.coffeecoders.questbot.models.ExtendedUpdate;

/**
 * @author ezuykow
 */
@Component
public class ChatMembersManager {

    private final ChatMembersActions chatMembersActions;

    public ChatMembersManager(ChatMembersActions actions) {
        this.chatMembersActions = actions;
    }

    /**
     *
     * @author ezuykow
     */
    public void manageChatMembers(ExtendedUpdate update, ExtendedUpdate.UpdateType type) {
        long chatId = update.getUpdatedMemberChatId();
        User updatedMember = update.getUpdatedMemberUser();
        switch (type) {
            case NEW_CHAT_MEMBER ->
                chatMembersActions.newChatMember(updatedMember, chatId);
            case LEFT_CHAT_MEMBER ->
                chatMembersActions.leftChatMember(updatedMember, chatId);
        }
    }
}

package ru.coffeecoders.questbot.managers;

import com.pengrad.telegrambot.model.ChatPermissions;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.AdminChatMembers;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.services.AdminChatMembersService;

import java.util.Arrays;

/**
 * @author ezuykow
 */
@Component
public class RestrictingManager {

    private final AdminChatMembersService adminChatMembersService;
    private final MessageSender msgSender;

    public RestrictingManager(AdminChatMembersService adminChatMembersService, MessageSender msgSender) {
        this.adminChatMembersService = adminChatMembersService;
        this.msgSender = msgSender;
    }

    public void restrictMembers(long chatId, long initiatorId) {
        switchPermissions(chatId, initiatorId, false);
    }

    public void unRestrictMembers(long chatId) {
        switchPermissions(chatId, -1, true);
    }

    private void switchPermissions(long chatId, long initiatorId, boolean promote) {
        AdminChatMembers chat = adminChatMembersService.findByChatId(chatId).orElseThrow(() ->
                new RuntimeException("Unknown chatId"));
        Arrays.stream(chat.getMembers())
                .filter(id -> id != initiatorId)
                .forEach(id ->
                        msgSender.sendRestrictChatMember(chatId, id, permissions(promote)));
    }

    private ChatPermissions permissions(boolean promote) {
        return new ChatPermissions()
                .canSendMessages(promote)
                .canSendOtherMessages(promote);
    }
}

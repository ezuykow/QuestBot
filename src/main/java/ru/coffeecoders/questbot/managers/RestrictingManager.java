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

    //-----------------API START-----------------

    /**
     * Вызывает {@link RestrictingManager#switchPermissions} c {@code promote = false}
     * @param chatId id чата
     * @param initiatorId id инициатора
     * @author ezuykow
     */
    public void restrictMembers(long chatId, long initiatorId) {
        switchPermissions(chatId, initiatorId, false);
    }

    /**
     * Вызывает {@link RestrictingManager#switchPermissions} с {@code promote = true} и {@code initiatorId = -1}
     * @param chatId id чата
     * @author ezuykow
     */
    public void unRestrictMembers(long chatId) {
        switchPermissions(chatId, -1, true);
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void switchPermissions(long chatId, long initiatorId, boolean promote) {
        AdminChatMembers chat = adminChatMembersService.findByChatId(chatId).orElseThrow(() ->
                new RuntimeException("Unknown chatId"));
        Arrays.stream(chat.getMembers())
                .filter(id -> id != initiatorId)
                .forEach(id ->
                        msgSender.sendRestrictChatMember(chatId, id, permissions(promote)));
    }

    /**
     * @author ezuykow
     */
    private ChatPermissions permissions(boolean promote) {
        return new ChatPermissions()
                .canSendMessages(promote)
                .canSendOtherMessages(promote);
    }
}

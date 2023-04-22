package ru.coffeecoders.questbot.managers;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.services.AdminChatService;

/**
 * @author ezuykow
 */
@Component
public class BlockingManager {

    private final AdminChatService adminChatService;

    public BlockingManager(AdminChatService adminChatService) {
        this.adminChatService = adminChatService;
    }

    public void blockAdminChatByAdmin(long chatId, long adminId) {
        switchBlocker(chatId, adminId);
    }

    public void unblockAdminChat(long chatId) {
        switchBlocker(chatId, 0);
    }

    private void switchBlocker(long chatId, long userId) {
        AdminChat chat = adminChatService.findById(chatId).orElseThrow( () ->
                new RuntimeException("Unknown chatId"));
        chat.setBlockedByAdminId(userId);
        adminChatService.save(chat);
    }
}

package ru.coffeecoders.questbot.managers;

import com.pengrad.telegrambot.model.User;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.exceptions.NonExistentChat;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.services.AdminChatService;

/**
 * @author ezuykow
 */
@Component
public class BlockingManager {

    private final AdminChatService adminChatService;
    private final MessageSender msgSender;

    public BlockingManager(AdminChatService adminChatService, MessageSender msgSender) {
        this.adminChatService = adminChatService;
        this.msgSender = msgSender;
    }

    //-----------------API START-----------------

    public void blockAdminChatByAdmin(long chatId, long adminId, String cause) {
        switchBlocker(chatId, adminId, cause);
    }

    public void unblockAdminChat(long chatId, String cause) {
        switchBlocker(chatId, 0, cause);
    }

    public long getBlockedAdminId(long chatId) {
        return adminChatService.findById(chatId)
                .orElseThrow(NonExistentChat::new)
                .getBlockedByAdminId();
    }

    //-----------------API END-----------------

    private void switchBlocker(long chatId, long userId, String cause) {
        AdminChat chat = adminChatService.findById(chatId).orElseThrow(NonExistentChat::new);
        chat.setBlockedByAdminId(userId);
        adminChatService.save(chat);
        msgSender.send(chatId, buildName(chatId, userId) + cause);
    }

    private String buildName(long chatId, long senderAdminId) {
        if (senderAdminId == 0) {
            return "";
        } else {
            User user = msgSender.getChatMember(chatId, senderAdminId);
            return (user.lastName() == null)
                    ? user.firstName()
                    : user.firstName() + " " + user.lastName();
        }
    }
}

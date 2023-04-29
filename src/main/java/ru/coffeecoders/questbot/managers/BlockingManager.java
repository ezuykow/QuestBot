package ru.coffeecoders.questbot.managers;

import com.pengrad.telegrambot.model.User;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.exceptions.NonExistentChat;
import ru.coffeecoders.questbot.logs.LogSender;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.services.AdminChatService;

/**
 * @author ezuykow
 */
@Component
public class BlockingManager {

    private final AdminChatService adminChatService;
    private final MessageSender msgSender;
    private final LogSender logger;

    public BlockingManager(AdminChatService adminChatService, MessageSender msgSender, LogSender logger) {
        this.adminChatService = adminChatService;
        this.msgSender = msgSender;
        this.logger = logger;
    }

    //-----------------API START-----------------

    /**
     * Вызывает {@link BlockingManager#switchBlocker}
     * @param chatId id блокируемого чата
     * @param adminId id блокирующего админа
     * @param cause причина блокировки
     * @author ezuykow
     */
    public void blockAdminChatByAdmin(long chatId, long adminId, String cause) {
        logger.warn("Блокирую чат");
        switchBlocker(chatId, adminId, cause);
    }

    /**
     * Вызывает {@link BlockingManager#switchBlocker} с {@code userid = 0}
     * @param chatId id разблокируемого чата
     * @param cause причина разблокировки
     * @author ezuykow
     */
    public void unblockAdminChat(long chatId, String cause) {
        switchBlocker(chatId, 0, cause);
    }

    /**
     * Возвращает id админа, заблокировавшего чат
     * @param chatId id чата
     * @return id админа, заблокировавшего чат
     * @author ezuykow
     */
    public long getBlockedAdminId(long chatId) {
        return adminChatService.findById(chatId)
                .orElseThrow(NonExistentChat::new)
                .getBlockedByAdminId();
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void switchBlocker(long chatId, long userId, String cause) {
        AdminChat chat = adminChatService.findById(chatId).orElseThrow(NonExistentChat::new);
        chat.setBlockedByAdminId(userId);
        adminChatService.save(chat);
        msgSender.send(chatId, buildName(chatId, userId) + cause);
    }

    /**
     * @author ezuykow
     */
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

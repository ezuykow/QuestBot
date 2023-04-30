package ru.coffeecoders.questbot.validators;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Admin;
import ru.coffeecoders.questbot.managers.BlockingManager;
import ru.coffeecoders.questbot.services.AdminChatService;
import ru.coffeecoders.questbot.services.AdminService;
import ru.coffeecoders.questbot.services.GlobalChatService;

/**
 * @author anna
 */
@Component
public class ChatAndUserValidator {

    private final AdminService adminService;
    private final AdminChatService adminChatService;
    private final GlobalChatService globalChatService;
    private final BlockingManager blockingManager;

    public ChatAndUserValidator(AdminService adminService,
                                AdminChatService adminChatService,
                                GlobalChatService globalChatService, BlockingManager blockingManager) {
        this.adminService = adminService;
        this.adminChatService = adminChatService;
        this.globalChatService = globalChatService;
        this.blockingManager = blockingManager;
    }

    /**
     * @param userId id пользователя
     * @return true, если userId принадлежит админу, иначе false
     * @author anna
     * @Redact: ezuykow
     */
    public boolean isAdmin(long userId) {
        return adminService.findById(userId).isPresent();
    }

    /**
     * @param userId id пользователя
     * @return true, если userId принадлежит владельцу, иначе false
     * @author ezuykow
     */
    public boolean isOwner(long userId) {
        return adminService.findById(userId).filter(Admin::isOwner).isPresent();
    }

    /**
     * @param chatId id чата
     * @param userId id пользователя
     * @return {@code true}, если {@code userId} принадлежит админу, заблокировавшему чат
     * с {@code id = chatId}, иначе {@code false}
     * @author ezuykow
     */
    public boolean isBlockedAdmin(long chatId, long userId) {
        return userId == blockingManager.getBlockedAdminId(chatId);
    }

    /**
     * @param chatId - id чата
     * @return true, если чат с таким id админский, иначе false
     */
    public boolean isAdminChat(long chatId) {
        return adminChatService.findById(chatId).isPresent();
    }

    /**
     * @param chatId - id чата
     * @return true, если чат с таким id игровой, иначе false
     */
    public boolean isGlobalChat(long chatId) {
        return globalChatService.findById(chatId).isPresent();
    }

    /**
     * @param chatId id чата
     * @return {@code true}, если чат с таким id есть в системе, иначе {@code false}
     * @author ezuykow
     */
    public boolean chatAlreadyAdded(long chatId) {
        return isAdminChat(chatId) || isGlobalChat(chatId);
    }

    /**
     * @param chatId id чата
     * @return {@code true}, если чата с таким id нет в системе, иначе {@code false}
     * @author ezuykow
     */
    public boolean chatNotAdded(long chatId) {
        return !chatAlreadyAdded(chatId);
    }
}

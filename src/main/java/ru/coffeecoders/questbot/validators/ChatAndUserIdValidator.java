package ru.coffeecoders.questbot.validators;

import ru.coffeecoders.questbot.services.AdminChatService;
import ru.coffeecoders.questbot.services.AdminService;
import ru.coffeecoders.questbot.services.GlobalChatService;

/**
 * @author anna
 */
public class ChatAndUserIdValidator {
    private final AdminService adminService;
    private final AdminChatService adminChatService;
    private final GlobalChatService globalChatService;

    public ChatAndUserIdValidator(AdminService adminService,
                                  AdminChatService adminChatService,
                                  GlobalChatService globalChatService) {
        this.adminService = adminService;
        this.adminChatService = adminChatService;
        this.globalChatService = globalChatService;
    }

    /**
     * @param userId - id пользователя
     * @return true, если userId принадлежит админу, иначе false
     */
    public boolean isAdmin(long userId) {
        return adminService.findById(userId).isPresent();
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
}

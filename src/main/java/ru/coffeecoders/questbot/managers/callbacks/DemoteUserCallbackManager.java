package ru.coffeecoders.questbot.managers.callbacks;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Admin;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.exceptions.NonExistentAdmin;
import ru.coffeecoders.questbot.exceptions.NonExistentChat;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.services.AdminChatService;
import ru.coffeecoders.questbot.services.AdminService;
import ru.coffeecoders.questbot.validators.ChatAndUserValidator;

import java.util.Set;

/**
 * @author ezuykow
 */
@Component
public class DemoteUserCallbackManager {

    private final AdminChatService adminChatService;
    private final AdminService adminService;
    private final ChatAndUserValidator validator;
    private final MessageSender msgSender;
    private final Messages messages;

    public DemoteUserCallbackManager(AdminChatService adminChatService, AdminService adminService,
                                     ChatAndUserValidator validator, MessageSender msgSender, Messages messages) {
        this.adminChatService = adminChatService;
        this.adminService = adminService;
        this.validator = validator;
        this.msgSender = msgSender;
        this.messages = messages;
    }

    //-----------------API START-----------------

    /**
     * Проверяет, что {@code senderUserId} это id владельца бота и вызывает
     * {@link DemoteUserCallbackManager#performDemotion}
     * @param senderUserId id пользователя, от которого пришел калбак
     * @param chatId id чата
     * @param msgId id сообщения
     * @param data данные калбака
     * @author ezuykow
     */
    public void manageCallback(long senderUserId, long chatId, int msgId, String data) {
        if (validator.isOwner(senderUserId)) {
            performDemotion(chatId, msgId, data);
        }
    }

    //-----------------API END-----------------
    
    /**
     * @author ezuykow
     */
    private void performDemotion(long chatId, int msgId, String data) {
        final long adminId = Long.parseLong(data.substring(data.lastIndexOf(".") + 1));
        deleteChooseUserMessage(chatId, msgId);
        deleteAdmin(chatId, adminId);
        adminService.deleteUselessAdmins();
        sendDemotionMessage(chatId, data);
    }

    /**
     * @author ezuykow
     */
    private void deleteAdmin(long chatId, long adminId) {
        AdminChat currentAdminChat = adminChatService.findById(chatId)
                .orElseThrow(NonExistentChat::new);
        Set<Admin> admins = currentAdminChat.getAdmins();
        admins.remove(admins.stream().filter(a -> a.getTgAdminUserId() == adminId).findAny()
                .orElseThrow(NonExistentAdmin::new));
        currentAdminChat.setAdmins(admins);
        adminChatService.save(currentAdminChat);
    }

    /**
     * @author ezuykow
     */
    private void deleteChooseUserMessage(long chatId, int msgId) {
        msgSender.sendDelete(chatId, msgId);
    }

    /**
     * @author ezuykow
     */
    private void sendDemotionMessage(long chatId, String data) {
        msgSender.send(chatId,
                data.substring(data.indexOf(".") + 1, data.lastIndexOf("."))
                        + messages.userDemoted()
        );
    }
}

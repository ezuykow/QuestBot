package ru.coffeecoders.questbot.managers.callbacks;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Admin;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.services.AdminChatService;
import ru.coffeecoders.questbot.validators.ChatAndUserValidator;

import java.util.Collections;
import java.util.Set;

/**
 * @author ezuykow
 */
@Component
public class PromoteUserCallbackManager {

    private final ChatAndUserValidator validator;
    private final AdminChatService adminChatService;
    private final MessageSender msgSender;
    private final Environment env;

    public PromoteUserCallbackManager(ChatAndUserValidator validator, AdminChatService adminChatService, MessageSender msgSender, Environment env) {
        this.validator = validator;
        this.adminChatService = adminChatService;
        this.msgSender = msgSender;
        this.env = env;
    }

    /**
     * @author ezuykow
     */
    public void manageCallback(ExtendedUpdate update, String data) {
        if (validator.isOwner(update.getCallbackFromUserId())) {
            performPromotion(update, data);
        }
    }

    /**
     * @author ezuykow
     */
    private void performPromotion(ExtendedUpdate update, String data) {
        final long chatId = update.getCallbackMessageChatId();
        final long newAdminId = Long.parseLong(data.substring(data.lastIndexOf(".") + 1));
        deleteChooseUserMessage(chatId, update.getCallbackMessageId());
        saveNewAdmin(chatId, newAdminId);
        sendPromotionMessage(chatId, data);
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
    private void saveNewAdmin(long chatId, long userId) {
        AdminChat currentAdminChat = adminChatService.findById(chatId).get();
        Set<Admin> admins = currentAdminChat.getAdmins();
        admins.add(new Admin(userId, Collections.singleton(currentAdminChat)));
        currentAdminChat.setAdmins(admins);
        adminChatService.save(currentAdminChat);
    }

    private void sendPromotionMessage(long chatId, String data) {
        msgSender.send(chatId,
                data.substring(data.indexOf(".") + 1, data.lastIndexOf("."))
                        + env.getProperty("messages.admins.userPromoted")
        );
    }
}

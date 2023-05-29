package ru.coffeecoders.questbot.managers.callbacks;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Admin;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.exceptions.NonExistentChat;
import ru.coffeecoders.questbot.messages.MessageBuilder;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
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
    private final Messages messages;
    private final MessageBuilder messageBuilder;

    public PromoteUserCallbackManager(ChatAndUserValidator validator, AdminChatService adminChatService,
                                      MessageSender msgSender, Messages messages, MessageBuilder messageBuilder) {
        this.validator = validator;
        this.adminChatService = adminChatService;
        this.msgSender = msgSender;
        this.messages = messages;
        this.messageBuilder = messageBuilder;
    }

    //-----------------API START-----------------

    /**
     * Проверяет, что {@code senderUserId} это id владельца бота и вызывает
     * {@link PromoteUserCallbackManager#performPromotion}
     * @param senderUserId id пользователя, от которого пришел калбак
     * @param chatId id чата
     * @param msgId id сообщения
     * @param data данные калбака
     * @author ezuykow
     */
    public void manageCallback(long senderUserId, long chatId, int msgId, String data) {
        if (validator.isOwner(senderUserId)) {
            performPromotion(chatId, msgId, data);
        }
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void performPromotion(long chatId, int msgId, String data) {
        final long newAdminId = Long.parseLong(data.substring(data.lastIndexOf(".") + 1));
        deleteChooseUserMessage(chatId, msgId);
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
        AdminChat currentAdminChat = adminChatService.findById(chatId)
                .orElseThrow(NonExistentChat::new);
        Set<Admin> admins = currentAdminChat.getAdmins();
        admins.add(new Admin(userId, Collections.singleton(currentAdminChat)));
        currentAdminChat.setAdmins(admins);
        adminChatService.save(currentAdminChat);
    }

    /**
     * @author ezuykow
     */
    private void sendPromotionMessage(long chatId, String data) {
        msgSender.send(chatId,
                messageBuilder.build(
                data.substring(data.indexOf(".") + 1, data.lastIndexOf(".")) + messages.userPromoted(),
                        chatId)
        );
    }
}

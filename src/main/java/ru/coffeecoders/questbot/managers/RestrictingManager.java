package ru.coffeecoders.questbot.managers;

import com.pengrad.telegrambot.model.ChatPermissions;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.AdminChatMembers;
import ru.coffeecoders.questbot.exceptions.NonExistentChat;
import ru.coffeecoders.questbot.logs.LogSender;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.services.AdminChatMembersService;
import ru.coffeecoders.questbot.validators.ChatAndUserValidator;

import java.util.Arrays;

/**
 * @author ezuykow
 */
@Component
public class RestrictingManager {

    private final AdminChatMembersService adminChatMembersService;
    private final ChatAndUserValidator validator;
    private final MessageSender msgSender;
    private final LogSender logger;

    public RestrictingManager(AdminChatMembersService adminChatMembersService, ChatAndUserValidator validator,
                              MessageSender msgSender, LogSender logger) {
        this.adminChatMembersService = adminChatMembersService;
        this.validator = validator;
        this.msgSender = msgSender;
        this.logger = logger;
    }

    //-----------------API START-----------------

    /**
     * Вызывает {@link RestrictingManager#switchPermissions} c {@code promote = false}
     * @param chatId id чата
     * @param initiatorId id инициатора
     * @author ezuykow
     */
    public void restrictMembers(long chatId, long initiatorId) {
        logger.warn("Ограничиваю пользователей");
        switchPermissions(chatId, initiatorId, false);
    }

    /**
     * Вызывает {@link RestrictingManager#switchPermissions} с {@code promote = true} и {@code initiatorId = -1}
     * @param chatId id чата
     * @author ezuykow
     */
    public void unRestrictMembers(long chatId) {
        logger.warn("Снимаю ограничение пользователей");
        switchPermissions(chatId, -1, true);
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void switchPermissions(long chatId, long initiatorId, boolean promote) {
        AdminChatMembers chat = adminChatMembersService.findByChatId(chatId)
                .orElseThrow(NonExistentChat::new);
        Arrays.stream(chat.getMembers())
                .filter(id -> id != initiatorId && !validator.isOwner(id))
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

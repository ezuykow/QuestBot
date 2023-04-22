package ru.coffeecoders.questbot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.services.AdminChatService;

import java.util.Optional;

/**
 * @author ezuykow
 */
@Component
public class BlockAdminChat {

    Logger logger = LoggerFactory.getLogger(BlockAdminChat.class);

    private final AdminChatService adminChatService;
    private final MessageSender msgSender;
    private final Environment env;

    public BlockAdminChat(AdminChatService adminChatService, MessageSender msgSender, Environment env) {
        this.adminChatService = adminChatService;
        this.msgSender = msgSender;
        this.env = env;
    }

    public void validateAndBlockAdminChatByAdmin(long adminChatId, long blockingAdminId) {
        Optional<AdminChat> currentAdminChatOpt = adminChatService.findById(adminChatId);
        if (currentAdminChatOpt.isPresent()) {
            blockAdminChatByAdmin(currentAdminChatOpt.get(), blockingAdminId);
        } else {
            logger.error("Validate failed in BlockAdminChat::validateAndBlockAdminChatByAdmin");
            msgSender.send(adminChatId, env.getProperty("messages.somethingWrong"));
        }

    }

    private void blockAdminChatByAdmin(AdminChat currentAdminChat, long blockingAdminId) {
        currentAdminChat.setBlockedByAdminId(blockingAdminId);
        adminChatService.save(currentAdminChat);
    }
}

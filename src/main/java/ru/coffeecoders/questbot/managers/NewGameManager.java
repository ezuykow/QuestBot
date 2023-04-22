package ru.coffeecoders.questbot.managers;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.utils.BlockAdminChat;

/**
 * @author ezuykow
 */
@Component
public class NewGameManager {

    private final BlockAdminChat blockAdminChat;

    public NewGameManager(BlockAdminChat blockAdminChat) {
        this.blockAdminChat = blockAdminChat;
    }

    public void startCreatingGame(long senderAdminId, long chatId) {
        blockAdminChat.validateAndBlockAdminChatByAdmin(chatId, senderAdminId);
    }
}

package ru.coffeecoders.questbot.managers;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.actions.NewGameActions;
import ru.coffeecoders.questbot.utils.BlockAdminChat;

/**
 * @author ezuykow
 */
@Component
public class NewGameManager {

    private final NewGameActions actions;
    private final BlockAdminChat blockAdminChat;

    public NewGameManager(NewGameActions actions, BlockAdminChat blockAdminChat) {
        this.actions = actions;
        this.blockAdminChat = blockAdminChat;
    }

    public void startCreatingGame(long senderAdminId, long chatId) {
        blockAdminChat.validateAndBlockAdminChatByAdmin(chatId, senderAdminId);
        actions.createNewGameCreatingState(chatId);
    }
}

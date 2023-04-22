package ru.coffeecoders.questbot.managers.game;

import org.springframework.stereotype.Component;

/**
 * @author ezuykow
 */
@Component
public class NewGameManager {

    public void startCreatingGame(long senderAdminId, long chatId) {
        blockAllOtherChatMembers(senderAdminId, chatId);
    }

    private void blockAllOtherChatMembers(long senderAdminId, long chatId) {

    }
}

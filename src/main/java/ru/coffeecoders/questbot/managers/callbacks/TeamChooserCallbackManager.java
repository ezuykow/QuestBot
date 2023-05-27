package ru.coffeecoders.questbot.managers.callbacks;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Player;
import ru.coffeecoders.questbot.exceptions.NonExistentChat;
import ru.coffeecoders.questbot.exceptions.NonExistentPlayer;
import ru.coffeecoders.questbot.services.GlobalChatService;
import ru.coffeecoders.questbot.services.PlayerService;
import ru.coffeecoders.questbot.viewers.TeamsViewer;

/**
 * @author ezuykow
 */
@Component
public class TeamChooserCallbackManager {

    private final PlayerService playerService;
    private final GlobalChatService globalChatService;
    private final TeamsViewer teamsViewer;

    public TeamChooserCallbackManager(PlayerService playerService, GlobalChatService globalChatService, TeamsViewer teamsViewer) {
        this.playerService = playerService;
        this.globalChatService = globalChatService;
        this.teamsViewer = teamsViewer;
    }

    //-----------------API START-----------------

    public void manageCallback(long senderUserId, long chatId, int msgId, String data) {
        String chosenTeamName = data.substring(data.lastIndexOf(".") + 1);
        String gameName = globalChatService.findById(chatId).orElseThrow(NonExistentChat::new)
                .getCreatingGameName();

        if (msgIsNotValid(chatId, msgId)) {
            return;
        }

        Player targetPlayer;
        try {
            targetPlayer = playerService.findById(senderUserId).orElseThrow(NonExistentPlayer::new);
            targetPlayer.setTeamName(chosenTeamName);
        } catch (NonExistentPlayer e) {
            targetPlayer = new Player(senderUserId, gameName, chosenTeamName, chatId);
        }
        playerService.save(targetPlayer);

        teamsViewer.refreshMsgInChat(chatId);
    }

    //-----------------API END-----------------

    private boolean msgIsNotValid(long chatId, int msgId) {
        return !teamsViewer.isMsgValid(chatId, msgId);
    }
}

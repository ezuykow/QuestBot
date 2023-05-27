package ru.coffeecoders.questbot.viewers;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.entities.Player;
import ru.coffeecoders.questbot.entities.Team;
import ru.coffeecoders.questbot.keyboards.viewers.TeamChooserKeyboard;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.services.AdminChatService;
import ru.coffeecoders.questbot.services.PlayerService;
import ru.coffeecoders.questbot.services.TeamService;

import java.util.*;

/**
 * @author ezuykow
 */
@Component
public class TeamsViewer {

    private record ViewerMeta(int msgId, InlineKeyboardMarkup keyboard) {}

    private final MessageSender msgSender;
    private final Messages messages;
    private final TeamService teamService;
    private final PlayerService playerService;
    private final AdminChatService adminChatService;

    private String msgHat;
    private final Map<Long, ViewerMeta> showedChoosers = new HashMap<>();

    public TeamsViewer(MessageSender msgSender, Messages messages, TeamService teamService, PlayerService playerService,
                       AdminChatService adminChatService) {
        this.msgSender = msgSender;
        this.messages = messages;
        this.teamService = teamService;
        this.playerService = playerService;
        this.adminChatService = adminChatService;
    }

    //-----------------API START-----------------

    public void showEmptyTeamChooser(long chatId) {
        msgHat = messages.teamChooserHat();
        String text = msgHat + teamsMsg(chatId);
        InlineKeyboardMarkup keyboard = TeamChooserKeyboard.createKeyboard(teamService.getTeamNamesByChatId(chatId));
        int msgId = msgSender.send(chatId, text, keyboard);
        msgSender.sendPinMessage(chatId, msgId);
        showedChoosers.put(chatId, new ViewerMeta(msgId, keyboard));
    }

    public void refreshMsgInChat(long chatId) {
        String text = msgHat + teamsMsg(chatId);
        ViewerMeta meta = showedChoosers.get(chatId);
        msgSender.edit(chatId, meta.msgId, text, meta.keyboard);
    }

    public void deleteShowedTeamsChooser(long chatId) {
        if (showedChoosers.containsKey(chatId)) {
            msgSender.sendDelete(chatId, showedChoosers.get(chatId).msgId);
            showedChoosers.remove(chatId);
        }
    }

    public boolean isMsgValid(long chatId, int msgId) {
        return showedChoosers.containsKey(chatId) && showedChoosers.get(chatId).msgId == msgId;
    }

    public void sendCommandsToAdmins(long chatId) {
        Chat gameChat = msgSender.sendGetChat(chatId);
        String hat = "In chat @" + gameChat.username() + " the game was started\n\n";
        String teams = teamsMsg(chatId);

        List<AdminChat> chats = adminChatService.findAll();
        chats.forEach(c -> msgSender.send(c.getTgAdminChatId(), hat + teams));
    }

    //-----------------API END-----------------

    private String teamsMsg(long chatId) {
        StringBuilder sb = new StringBuilder();
        getPlayersNamesMappedByTeamsNames(chatId).forEach((teamName, playersNames) -> {
            sb.append("\uD83D\uDC65").append(teamName).append(":\n");
            if (playersNames.size() == 0) {
                sb.append("  <--->\n");
            }
            for (int i = 0; i < playersNames.size(); i++) {
                sb.append("  ").append(i + 1).append(". ").append(playersNames.get(i)).append("\n");
            }
            sb.append("\n");
        });
        return sb.toString();
    }

    private Map<String, List<String>> getPlayersNamesMappedByTeamsNames(long chatId) {
        Map<String, List<String>> playersNamesMappedByTeamsNames = new LinkedHashMap<>();

        List<Team> teams = teamService.findByChatId(chatId);
        List<Player> players = playerService.findAllByChatId(chatId);

        teams.forEach(t -> playersNamesMappedByTeamsNames.put(t.getTeamName(), new ArrayList<>()));
        players.forEach(p -> {
            User playerUser = msgSender.getChatMember(chatId, p.getTgUserId());
            String name = (playerUser.lastName() == null)
                    ? playerUser.firstName()
                    : playerUser.firstName() + " " + playerUser.lastName();
            playersNamesMappedByTeamsNames.get(p.getTeamName()).add(name);
        });

        return playersNamesMappedByTeamsNames;
    }
}

package ru.coffeecoders.questbot.viewers;

import com.pengrad.telegrambot.model.Chat;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.entities.Team;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.services.*;

import java.util.Comparator;
import java.util.List;

/**
 * @author ezuykow
 */
@Component
public class EndGameViewer {

    private final TeamService teamService;
    private final TaskService taskService;
    private final PlayerService playerService;
    private final GlobalChatService globalChatService;
    private final AdminChatService adminChatService;
    private final MessageSender msgSender;

    public EndGameViewer(TeamService teamService, TaskService taskService, PlayerService playerService, GlobalChatService globalChatService, AdminChatService adminChatService, MessageSender msgSender) {
        this.teamService = teamService;
        this.taskService = taskService;
        this.playerService = playerService;
        this.globalChatService = globalChatService;
        this.adminChatService = adminChatService;
        this.msgSender = msgSender;
    }

    //-----------------API START-----------------

    /**
     * Заканчивает игру, в которой вышло время - формирует результаты и отправляет сообщения с
     * ними в чат и в админские чаты
     * @param chat id чата, где закончилось время на игру
     * @author ezuykow
     */
    public void finishGameByTimeEnded(GlobalChat chat) {
        long chatId = chat.getTgChatId();
        String results = results(chatId);
        notifyGlobalChat(chatId, results);
        notifyAdminChats(chatId, results);
        clearDB(chat);
    }

    //-----------------API END-----------------
    /**
     * @author ezuykow
     */
    private void clearDB(GlobalChat chat) {
        long chatId = chat.getTgChatId();
        playerService.deleteAllByChatId(chatId);
        teamService.deleteAllByChatId(chatId);
        taskService.deleteAllByChatId(chatId);
        chat.setGameStarted(false);
        chat.setCreatingGameName(null);
        chat.setMinutesSinceStart(0);
        globalChatService.save(chat);
    }

    /**
     * @author ezuykow
     */
    private String results(long chatId) {
        StringBuilder sb = new StringBuilder();
        sb.append("✔ Результаты:\n");
        List<Team> teams = teamService.findByChatId(chatId);
        teams.sort(Comparator.comparing(Team::getScore, Comparator.naturalOrder()));
        for (int place = 1; place <= teams.size(); place++) {
            Team team = teams.get(place - 1);
            switch (place) {
                case 1 -> sb.append("\uD83E\uDD47 ");
                case 2 -> sb.append("\uD83E\uDD48 ");
                case 3 -> sb.append("\uD83E\uDD49 ");
                default -> sb.append(place).append(" ");
            }
            sb.append("Команда \"").append(team.getTeamName()).append("\" заработала очков: ")
                    .append(team.getScore());
        }
        return sb.toString();
    }

    /**
     * @author ezuykow
     */
    private void notifyGlobalChat(long chatId, String results) {
        String text = "⏰ Время игры вышло! ⏰\n\n" + results +
                "\n\n\uD83D\uDC4D Всем участникам спасибо за игру! Надеемся, Вам понравилось \uD83D\uDE1C";
        msgSender.send(chatId, text);
    }

    /**
     * @author ezuykow
     */
    private void notifyAdminChats(long chatId, String results) {
        Chat chat = msgSender.sendGetChat(chatId);
        String text = "В чате @" + chat.username() + " закончена игра!\n" + results;
        List<AdminChat> chats = adminChatService.findAll();
        chats.forEach(c -> msgSender.send(c.getTgAdminChatId(), text));
    }
}

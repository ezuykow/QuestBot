package ru.coffeecoders.questbot.viewers;

import com.pengrad.telegrambot.model.Chat;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.entities.PinnedTasksMessage;
import ru.coffeecoders.questbot.entities.Team;
import ru.coffeecoders.questbot.exceptions.NonExistentChat;
import ru.coffeecoders.questbot.messages.MessageBuilder;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
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
    private final PinnedTasksMessageService pinnedTasksMessageService;
    private final MessageSender msgSender;
    private final Messages messages;
    private final MessageBuilder messageBuilder;

    public EndGameViewer(TeamService teamService, TaskService taskService, PlayerService playerService,
                         GlobalChatService globalChatService, AdminChatService adminChatService,
                         PinnedTasksMessageService pinnedTasksMessageService, MessageSender msgSender,
                         Messages messages, MessageBuilder messageBuilder) {
        this.teamService = teamService;
        this.taskService = taskService;
        this.playerService = playerService;
        this.globalChatService = globalChatService;
        this.adminChatService = adminChatService;
        this.pinnedTasksMessageService = pinnedTasksMessageService;
        this.msgSender = msgSender;
        this.messages = messages;
        this.messageBuilder = messageBuilder;
    }

    //-----------------API START-----------------

    /**
     * Заканчивает игру, в которой вышло время - формирует результаты и отправляет сообщения с
     * ними в чат и в админские чаты
     * @param chat id чата, где закончилось время на игру
     * @author ezuykow
     */
    public void finishGameByTimeEnded(GlobalChat chat) {
        finishGame(messages.endGameTime(), chat.getTgChatId());
    }

    /**
     * Заканчивает игру, в которой одна из команд ответила на необходимое количество вопросов
     * - формирует результаты и отправляет сообщения с ними в чат и в админские чаты
     * @param chatId id чата
     * @param teamName название команды
     * @param score очки команды
     * @author ezuykow
     */
    public void finishGameByPerformedTasks(long chatId, String teamName, int score) {
        finishGame(messages.endGameTeamPrefix() + teamName
                        + messages.endGameTeamMiddle() + score
                        + messages.endGameTeamPostfix(),
                chatId);
    }

    /**
     * Заканчивает игру, в которой закончились вопросы
     * - формирует результаты и отправляет сообщения с ними в чат и в админские чаты
     * @param chatId id чата
     * @author ezuykow
     */
    public void finishGameByQuestionsEnded(long chatId) {
        finishGame(messages.endGameQuestionsEnded(), chatId);
    }

    /**
     * Заканчивает игру по команде админа
     * - формирует результаты и отправляет сообщения с ними в чат и в админские чаты
     * @param chatId id чата
     * @author ezuykow
     */
    public void finishGameByAdminsCmd(long chatId, String adminUsername) {
        finishGame("❗ " + adminUsername + messages.endGameInterrupt() + " ❗", chatId);
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void finishGame(String cause, long chatId) {
        msgSender.sendUnPinAllMessages(chatId);
        List<PinnedTasksMessage> tasksMessages = pinnedTasksMessageService.findAllByChatId(chatId);
        tasksMessages.forEach(m -> msgSender.sendDelete(chatId, m.getMsgId()));

        notifyGlobalChat(cause, chatId);
        notifyAdminChats(chatId);
        clearDB(chatId);
    }

    /**
     * @author ezuykow
     */
    private void clearDB(long chatId) {
        GlobalChat chat = globalChatService.findById(chatId).orElseThrow(NonExistentChat::new);
        playerService.deleteAllByChatId(chatId);
        teamService.deleteAllByChatId(chatId);
        taskService.deleteAllByChatId(chatId);
        pinnedTasksMessageService.deleteAllByChatId(chatId);
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
        teams.sort(Comparator.comparing(Team::getScore, Comparator.reverseOrder()));
        for (int place = 1; place <= teams.size(); place++) {
            Team team = teams.get(place - 1);
            switch (place) {
                case 1 -> sb.append("\uD83E\uDD47 ");
                case 2 -> sb.append("\uD83E\uDD48 ");
                case 3 -> sb.append("\uD83E\uDD49 ");
                default -> sb.append(place).append(" ");
            }
            sb.append(messages.endGameTeamPrefix()).append(team.getTeamName())
                    .append(messages.endGameTeamMiddle()).append(team.getScore())
                    .append(messages.endGameTeamPostfix()).append("\n");
        }
        return sb.toString();
    }

    /**
     * @author ezuykow
     */
    private void notifyGlobalChat(String cause, long chatId) {
        msgSender.send(chatId,
                messageBuilder.build(cause + messages.endGamePostfix(), chatId));
    }

    /**
     * @author ezuykow
     */
    private void notifyAdminChats(long chatId) {
        String results = results(chatId);
        Chat chat = msgSender.sendGetChat(chatId);
        String text = "In chat @" + chat.username() + " the game was ended!\n";
        List<AdminChat> chats = adminChatService.findAll();
        chats.forEach(c -> {
            msgSender.send(c.getTgAdminChatId(), text);
            msgSender.send(c.getTgAdminChatId(), results);
        });
    }
}

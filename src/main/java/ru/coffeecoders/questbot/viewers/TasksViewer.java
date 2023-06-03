package ru.coffeecoders.questbot.viewers;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.PinnedTasksMessage;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.entities.Task;
import ru.coffeecoders.questbot.exceptions.NonExistentChat;
import ru.coffeecoders.questbot.exceptions.NonExistentGame;
import ru.coffeecoders.questbot.exceptions.NonExistentQuestion;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.services.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author ezuykow
 */
@Component
public class TasksViewer {

    private final TaskService taskService;
    private final GameService gameService;
    private final GlobalChatService globalChatService;
    private final QuestionService questionService;
    private final PinnedTasksMessageService pinnedTasksMessageService;
    private final MessageSender msgSender;

    public TasksViewer(TaskService taskService, GameService gameService, GlobalChatService globalChatService,
                       QuestionService questionService, PinnedTasksMessageService pinnedTasksMessageService, MessageSender msgSender) {
        this.taskService = taskService;
        this.gameService = gameService;
        this.globalChatService = globalChatService;
        this.questionService = questionService;
        this.pinnedTasksMessageService = pinnedTasksMessageService;
        this.msgSender = msgSender;
    }

    //-----------------API START-----------------

    /**
     * Выводит список вопросов в чат
     * @param chatId id чата
     * @param count количество вопросов для вывода
     * @author ezuykow
     */
    public void createAndSendTasksMsg(long chatId, int count) {
        List<Task> tasks = taskService.findByChatId(chatId).stream()
                .filter(t -> t.getPerformedTeamName() == null)
                .sorted(Comparator.comparing(Task::getTaskNumber))
                .limit(count)
                .toList();
        tasks.forEach(t -> t.setActual(true));
        taskService.saveAll(tasks);
        List<String> allTasks = createMsg(tasks, chatId);
        for (String tasksPart : allTasks) {
            int msgId = msgSender.send(chatId, tasksPart);
            msgSender.sendPinMessage(chatId, msgId);
            pinnedTasksMessageService.save(new PinnedTasksMessage(msgId, chatId));
        }
    }

    /**
     * Выводит в чат актуальные для игры вопросы
     * @param chatId id чата
     * @author ezuykow
     */
    public void showActualTasks(long chatId) {
        List<Task> tasks = new ArrayList<>(taskService.findActualTasksByChatId(chatId));
        if (!tasks.isEmpty()) {
            tasks.sort(Comparator.comparingInt(Task::getTaskNumber));
            List<String> allTasks = createMsg(tasks, chatId);

            List<PinnedTasksMessage> pinnedMsgs = new ArrayList<>(pinnedTasksMessageService.findAllByChatId(chatId));
            pinnedMsgs.sort(Comparator.comparingInt(PinnedTasksMessage::getMsgId));

            int i = 0;
            for ( ; i < Math.min(pinnedMsgs.size(), allTasks.size()); i++) {
                msgSender.edit(chatId, pinnedMsgs.get(i).getMsgId(), allTasks.get(i));
            }
            for (int j = i; j < pinnedMsgs.size(); j++) {
                int msgId = pinnedMsgs.get(j).getMsgId();
                msgSender.sendUnPinMessage(chatId, msgId);
                msgSender.sendDelete(chatId, msgId);
                pinnedTasksMessageService.deleteByMsgId(msgId);
            }
            for (int j = i; j < allTasks.size(); j++) {
                int msgId = msgSender.send(chatId, allTasks.get(j));
                msgSender.sendPinMessage(chatId, msgId);
            }
        }
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private List<String> createMsg(List<Task> tasks, long chatId) {
        List<String> allTasks = new ArrayList<>(5);

        StringBuilder sb = new StringBuilder();
        for (Task task : tasks) {
            Question q = questionService.findById(task.getQuestionId()).orElseThrow(NonExistentQuestion::new);
            sb.append("\uD83C\uDFAF Вопрос № ").append(task.getTaskNumber()).append("\n")
                    .append("❓ ").append(q.getQuestion()).append("\n")
                    .append(answerFormat(q))
                    .append(additional(q, chatId));
            if (sb.length() >= 3000) {
                allTasks.add(sb.toString());
                sb = new StringBuilder();
            }
        }
        if (!sb.isEmpty()) {
            allTasks.add(sb.toString());
        }
        return allTasks;
    }

    /**
     * @author ezuykow
     */
    private String answerFormat(Question q) {
        if (q.getAnswerFormat() != null) {
            return "❗ Формат ответа: " + q.getAnswerFormat() + "\n";
        }
        return "";
    }

    /**
     * @author ezuykow
     */
    private String additional(Question q, long chatId) {
        boolean additionalNeeded = gameService.findByName(globalChatService.findById(chatId)
                .orElseThrow(NonExistentChat::new).getCreatingGameName())
                .orElseThrow(NonExistentGame::new).isAdditionWithTask();
        if (additionalNeeded && q.getAdditional() != null) {
            return "➕ Доп. информация: "+ q.getAdditional()+ "\n\n";
        }
        return "\n";
    }
}

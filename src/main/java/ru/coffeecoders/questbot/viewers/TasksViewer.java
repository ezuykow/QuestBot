package ru.coffeecoders.questbot.viewers;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.entities.Task;
import ru.coffeecoders.questbot.exceptions.NonExistentChat;
import ru.coffeecoders.questbot.exceptions.NonExistentGame;
import ru.coffeecoders.questbot.exceptions.NonExistentQuestion;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.services.GameService;
import ru.coffeecoders.questbot.services.GlobalChatService;
import ru.coffeecoders.questbot.services.QuestionService;
import ru.coffeecoders.questbot.services.TaskService;

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
    private final MessageSender msgSender;

    public TasksViewer(TaskService taskService, GameService gameService, GlobalChatService globalChatService, QuestionService questionService, MessageSender msgSender) {
        this.taskService = taskService;
        this.gameService = gameService;
        this.globalChatService = globalChatService;
        this.questionService = questionService;
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
                .limit(count)
                .toList();
        tasks.forEach(t -> t.setActual(true));
        taskService.saveAll(tasks);
        String[] allTasks = createMsg(tasks, chatId);
        for (String allTask : allTasks) {
            if (allTask != null) {
                msgSender.send(chatId, allTask);
            }
        }
    }

    /**
     * Выводит в чат актуальные для игры вопросы
     * @param chatId id чата
     * @author ezuykow
     */
    public void showActualTasks(long chatId) {
        List<Task> tasks = taskService.findActualTasksByChatId(chatId);
        if (!tasks.isEmpty()) {
            String[] allTasks = createMsg(tasks, chatId);
            for (String allTask : allTasks) {
                if (allTask != null) {
                    msgSender.send(chatId, allTask);
                }
            }
        }
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private String[] createMsg(List<Task> tasks, long chatId) {
        String[] allTasks = new String[10];

        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (Task task : tasks) {
            Question q = questionService.findById(task.getQuestionId()).orElseThrow(NonExistentQuestion::new);
            sb.append("\uD83C\uDFAF Вопрос № ").append(task.getTaskNumber()).append("\n")
                    .append("❓ ").append(q.getQuestion()).append("\n")
                    .append(answerFormat(q))
                    .append(additional(q, chatId));
            if (sb.length() >= 3000) {
                allTasks[i++] = sb.toString();
                sb = new StringBuilder();
            }
        }
        allTasks[i] = sb.toString();
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

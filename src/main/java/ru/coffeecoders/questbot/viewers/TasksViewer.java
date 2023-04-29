package ru.coffeecoders.questbot.viewers;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.entities.Task;
import ru.coffeecoders.questbot.exceptions.NonExistentQuestion;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.services.QuestionService;
import ru.coffeecoders.questbot.services.TaskService;

import java.util.List;

/**
 * @author ezuykow
 */
@Component
public class TasksViewer {

    private final TaskService taskService;
    private final QuestionService questionService;
    private final MessageSender msgSender;

    public TasksViewer(TaskService taskService, QuestionService questionService, MessageSender msgSender) {
        this.taskService = taskService;
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
        msgSender.sendWithHTML(chatId, createMsg(tasks));
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private String createMsg(List<Task> tasks) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            Question q = questionService.findById(tasks.get(i).getQuestionId()).orElseThrow(NonExistentQuestion::new);
            sb.append("\uD83C\uDFAF Вопрос № ").append(i + 1).append("\n")
                    .append("❓ ").append(q.getQuestion()).append("\n")
                    .append("❗ Формат ответа: ").append(q.getAnswerFormat()).append("\n");
            if (q.getMapUrl() == null) {
                sb.append("\n");
            } else {
                sb.append("\uD83D\uDDFA <a href=\"").append(q.getMapUrl()).append("\">На карте</a>\n\n");
            }
        }
        return sb.toString();
    }
}

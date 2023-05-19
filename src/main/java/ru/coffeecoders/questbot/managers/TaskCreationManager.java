package ru.coffeecoders.questbot.managers;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.entities.Task;
import ru.coffeecoders.questbot.exceptions.NonExistentQuestionGroup;
import ru.coffeecoders.questbot.logs.LogSender;
import ru.coffeecoders.questbot.services.QuestionGroupService;
import ru.coffeecoders.questbot.services.QuestionService;
import ru.coffeecoders.questbot.services.TaskService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ezuykow
 */
@Component
public class TaskCreationManager {

    private final QuestionService questionService;
    private final QuestionGroupService questionGroupService;
    private final TaskService taskService;
    private final LogSender logger;

    public TaskCreationManager(QuestionService questionService, QuestionGroupService questionGroupService,
                               TaskService taskService, LogSender logger) {
        this.questionService = questionService;
        this.questionGroupService = questionGroupService;
        this.taskService = taskService;
        this.logger = logger;
    }

    //-----------------API START-----------------

    /**
     * Создает задачи для подготавливаемой игры
     * @param chatId id чата
     * @param game подготавливаемая игра
     * @author ezuykow
     */
    public void createTasks(long chatId, Game game) {
        logger.warn("Создаю задачи");
        String gameName = game.getGameName();
        List<Question> questions = getSortedByLastUsageQuestionsByGroupsIds(game.getGroupsIds(), game.getMaxQuestionsCount());

        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            tasks.add(new Task(gameName, questions.get(i).getQuestionId(), null, chatId, i + 1));
        }

        taskService.saveAll(tasks);
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private List<Question> getSortedByLastUsageQuestionsByGroupsIds(int[] groupsIds, int maxQuestionsCount) {
        Set<String> groupNames = getGroupNames(groupsIds);
        return questionService.findAll().stream().filter(q -> groupNames.contains(q.getGroup()))
                .sorted(Comparator.comparing(Question::getLastUsage, Comparator.nullsFirst(Comparator.naturalOrder())))
                .limit(maxQuestionsCount)
                .toList();
    }

    /**
     * @author ezuykow
     */
    private Set<String> getGroupNames(int[] groupsIds) {
        return Arrays.stream(groupsIds)
                .mapToObj(id -> questionGroupService.findById(id)
                        .orElseThrow(NonExistentQuestionGroup::new)
                        .getGroupName())
                .collect(Collectors.toSet());
    }
}

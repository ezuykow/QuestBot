package ru.coffeecoders.questbot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.entities.Task;
import ru.coffeecoders.questbot.repositories.TaskRepository;

import java.util.List;

@Service
public class TaskService {

    Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll() {
        List<Task> taskList = taskRepository.findAll();
        if (!taskList.isEmpty()) {
            logger.info("Tasks are displaying");
        } else {
            logger.warn("No tasks found");
        }
        return taskList;
    }

    public void deleteById(int id) {
        if (taskRepository.existsById(id)) {
            logger.info("Task with id = {} has been deleted", id);
            taskRepository.deleteById(id);
            return;
        }
        logger.warn("There is no task with id = {}", id);
        throw new IllegalArgumentException("Task with id = " + id + " not found");
    }

    public Task save(Task task) {
        logger.info("Task = {} has been saved", task);
        return taskRepository.save(task);
    }

    public List<Task> findByGameName(String gameName) {
        List<Task> list = taskRepository.findByGameName(gameName);
        logger.info("Tasks {} displaying", list.isEmpty() ? "are not" : "are");
        return list;
    }

}

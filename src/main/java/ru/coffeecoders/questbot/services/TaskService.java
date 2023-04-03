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
        List<Task> list = taskRepository.findAll();
        logger.info("Tasks {} displaying", list.isEmpty() ? "are not" : "are");
        return list;
    }

    public boolean deleteById(int id) {
        if (taskRepository.existsById(id)) {
            logger.info("Task with id = {} has been deleted", id);
            taskRepository.deleteByTaskId(id);
            return true;
        }
        logger.warn("There is no task with id = {}", id);
        return false;
    }

    public Task save(Task task) {
        logger.info("Task = {} has been saved", task);
        return taskRepository.save(task);
    }
}

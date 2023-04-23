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

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> findAll() {
        List<Task> list = repository.findAll();
        logger.info("Tasks are {} displaying", list.isEmpty() ? "not" : "");
        return list;
    }

    public void deleteById(long id) {
        logger.info("Task with id = {} has been deleted", id);
        repository.deleteById(id);
    }

    public Task save(Task task) {
        logger.info("Task = {} has been saved", task);
        return repository.save(task);
    }

    public List<Task> findByGameName(String gameName) {
        List<Task> list = repository.findByGameName(gameName);
        logger.info("Tasks with game name = {} are {} displaying", gameName, list.isEmpty() ? "not" : "");
        return list;
    }
}
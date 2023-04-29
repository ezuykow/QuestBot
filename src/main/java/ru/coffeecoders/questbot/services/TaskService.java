package ru.coffeecoders.questbot.services;

import jakarta.transaction.Transactional;
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

    public List<Task> findByGameName(String gameName) {
        List<Task> list = repository.findByGameName(gameName);
        logger.info("Tasks with game name = {} are {} displaying", gameName, list.isEmpty() ? "not" : "");
        return list;
    }

    /**
     * @author ezuykow
     */
    public List<Task> findByChatId(long chatId) {
        return repository.findByChatId(chatId);
    }

    public Task save(Task task) {
        logger.info("Task = {} has been saved", task);
        return repository.save(task);
    }

    /**
     * @author ezuykow
     */
    public void saveAll(List<Task> tasks) {
        logger.info("Tasks = {} has been saved", tasks);
        repository.saveAll(tasks);
    }

    public void deleteById(long id) {
        logger.info("Task with id = {} has been deleted", id);
        repository.deleteById(id);
    }

    /**
     * @author ezuykow
     */
    @Transactional
    public void deleteAllByChatId(long chatId) {
        logger.info("Tasks with chatId = {} has been deleted", chatId);
        repository.deleteAllByChatId(chatId);
    }
}
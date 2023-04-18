package ru.coffeecoders.questbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.coffeecoders.questbot.entities.Task;

import java.util.List;

/**
 * @author ezuykow
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

//    void deleteByTaskId(int taskId);

    List<Task> findByGameName(String gameName);
}

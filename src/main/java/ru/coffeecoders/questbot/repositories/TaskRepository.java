package ru.coffeecoders.questbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.coffeecoders.questbot.entities.Task;

/**
 * @author ezuykow
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    void deleteByTaskId(Integer taskId);
}

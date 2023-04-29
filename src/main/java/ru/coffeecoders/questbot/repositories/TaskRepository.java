package ru.coffeecoders.questbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.coffeecoders.questbot.entities.Task;

import java.util.List;

/**
 * @author ezuykow
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * @author ezuykow
     */
    List<Task> findByGameName(String gameName);

    /**
     * @author ezuykow
     */
    List<Task> findByChatId(long chatId);

    /**
     * @author ezuykow
     */
    @Modifying
    @Query(value = "DELETE FROM tasks WHERE chat_id=:chatId", nativeQuery = true)
    void deleteAllByChatId(long chatId);
}

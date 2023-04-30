package ru.coffeecoders.questbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.coffeecoders.questbot.entities.Player;
import ru.coffeecoders.questbot.entities.Task;

import java.util.List;

/**
 * @author ezuykow
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Modifying
    @Query(value = "DELETE FROM players WHERE chat_id=:chatId", nativeQuery = true)
    void deleteAllByChatId(long chatId);
}

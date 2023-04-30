package ru.coffeecoders.questbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.coffeecoders.questbot.entities.Team;

import java.util.List;

/**
 * @author ezuykow
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, String> {

    /**
     * @author ezuykow
     */
    List<Team> findByChatId(long chatId);

    /**
     * @author ezuykow
     */
    @Modifying
    @Query(value = "DELETE FROM teams WHERE chat_id=:chatId", nativeQuery = true)
    void deleteAllByChatId(long chatId);
}

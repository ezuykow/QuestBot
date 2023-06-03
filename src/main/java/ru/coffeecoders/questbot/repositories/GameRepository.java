package ru.coffeecoders.questbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.coffeecoders.questbot.entities.Game;

import java.util.Optional;

/**
 * @author ezuykow
 */
@Repository
public interface GameRepository extends JpaRepository<Game, String> {

    Optional<Game> findByGameName(String gameName);

    Optional<Game> findByGameId(int gameId);

    /**
     * @author ezuykow
     */
    @Modifying
    @Query(value = "DELETE FROM games WHERE game_name=:gameName", nativeQuery = true)
    void deleteByGameName(String gameName);
}

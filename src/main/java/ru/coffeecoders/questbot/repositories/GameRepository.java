package ru.coffeecoders.questbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.coffeecoders.questbot.entities.Game;

/**
 * @author ezuykow
 */
@Repository
public interface GameRepository extends JpaRepository<Game, String> {
}

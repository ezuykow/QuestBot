package ru.coffeecoders.questbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.coffeecoders.questbot.entities.NewGameCreatingState;

/**
 * @author ezuykow
 */
@Repository
public interface NewGameCreatingStateRepository extends JpaRepository<NewGameCreatingState, Long> {
}

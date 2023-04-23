package ru.coffeecoders.questbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.coffeecoders.questbot.entities.Team;

/**
 * @author ezuykow
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, String> {

}

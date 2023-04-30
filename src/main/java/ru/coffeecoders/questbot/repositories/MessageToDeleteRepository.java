package ru.coffeecoders.questbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.coffeecoders.questbot.entities.MessageToDelete;

import java.util.List;

/**
 * @author ezuykow
 */
@Repository
public interface MessageToDeleteRepository extends JpaRepository<MessageToDelete, Integer> {

    List<MessageToDelete> findByUserId(long userId);
}

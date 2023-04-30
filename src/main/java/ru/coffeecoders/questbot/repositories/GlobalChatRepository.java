package ru.coffeecoders.questbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.coffeecoders.questbot.entities.GlobalChat;

/**
 * @author ezuykow
 */
@Repository
public interface GlobalChatRepository extends JpaRepository<GlobalChat, Long> {

}

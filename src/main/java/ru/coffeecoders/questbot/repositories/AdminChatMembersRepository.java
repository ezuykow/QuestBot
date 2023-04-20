package ru.coffeecoders.questbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.coffeecoders.questbot.entities.AdminChatMembers;

/**
 * @author ezuykow
 */
@Repository
public interface AdminChatMembersRepository extends JpaRepository<AdminChatMembers, Long> {
}

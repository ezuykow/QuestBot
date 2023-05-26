package ru.coffeecoders.questbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.coffeecoders.questbot.entities.PinnedTasksMessage;

import java.util.List;

/**
 * @author ezuykow
 */
@Repository
public interface PinnedTasksMessageRepository extends JpaRepository<PinnedTasksMessage, Integer> {

    List<PinnedTasksMessage> findAllByChatId(long chatId);

    void deleteAllByChatId(long chatId);

    void deleteByMsgId(int msgId);
}

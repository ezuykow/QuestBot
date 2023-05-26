package ru.coffeecoders.questbot.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.entities.PinnedTasksMessage;
import ru.coffeecoders.questbot.repositories.PinnedTasksMessageRepository;

import java.util.List;

/**
 * @author ezuykow
 */
@Service
public class PinnedTasksMessageService {

    private final PinnedTasksMessageRepository repository;

    public PinnedTasksMessageService(PinnedTasksMessageRepository repository) {
        this.repository = repository;
    }

    //-----------------API START-----------------

    public List<PinnedTasksMessage> findAllByChatId(long chatId) {
        return repository.findAllByChatId(chatId);
    }

    public void save(PinnedTasksMessage message) {
        repository.save(message);
    }

    @Transactional
    public void deleteByMsgId(int msgId) {
        repository.deleteByMsgId(msgId);
    }

    @Transactional
    public void deleteAllByChatId(long chatId) {
        repository.deleteAllByChatId(chatId);
    }

    //-----------------API END-----------------

}

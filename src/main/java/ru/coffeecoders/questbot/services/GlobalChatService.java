package ru.coffeecoders.questbot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.repositories.GlobalChatRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GlobalChatService {

    Logger logger = LoggerFactory.getLogger(GlobalChatService.class);

    private final GlobalChatRepository repository;

    public GlobalChatService(GlobalChatRepository repository) {
        this.repository = repository;
    }

    public List<GlobalChat> findAll() {
        List<GlobalChat> list = repository.findAll();
        logger.info("GlobalChats = {} are {} displaying", list, list.isEmpty() ? "not" : "");
        return list;
    }

    public Optional<GlobalChat> findById(long id) {
        Optional<GlobalChat> optional = repository.findById(id);
        logger.info("GlobalChat with id = {} {} found", id, optional.isPresent() ? "" : "not");
        return optional;
    }

    public GlobalChat save(GlobalChat globalChat) {
        logger.info("GlobalChat = {} has been saved", globalChat);
        return repository.save(globalChat);
    }

    /**
     * @author ezuykow
     */
    public void saveAll(List<GlobalChat> chats) {
        repository.saveAll(chats);
    }

    /**
     * @author ezuykow
     */
    public void deleteById(long chatId) {
        logger.info("GlobalChat with id = {} has been deleted", chatId);
        repository.deleteById(chatId);
    }
}
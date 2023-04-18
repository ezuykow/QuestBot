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
        logger.info("GlobalChats {} displaying", list.isEmpty() ? "are not" : "are");
        return list;
    }

    public Optional<GlobalChat> findById(long id) {
        Optional<GlobalChat> optional = repository.findById(id);
        logger.info("GlobalChat {} with id = {}", optional.isPresent() ? "found" : "not found", id);
        return optional;
    }

    public GlobalChat save(GlobalChat globalChat) {
        logger.info("GlobalChat = {} has been saved", globalChat);
        return repository.save(globalChat);
    }
}
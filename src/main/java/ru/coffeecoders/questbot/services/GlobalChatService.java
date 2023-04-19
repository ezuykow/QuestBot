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
    private final GlobalChatRepository globalChatRepository;
    public GlobalChatService(GlobalChatRepository globalChatRepository) {
        this.globalChatRepository = globalChatRepository;
    }

    public List<GlobalChat> findAll() {
        List<GlobalChat> list = globalChatRepository.findAll();
        logger.info("GlobalChats {} displaying", list.isEmpty() ? "are not" : "are");
        return list;
    }

    public Optional<GlobalChat> findById(long id) {
        Optional<GlobalChat> globalChat = globalChatRepository.findById(id);
        logger.info("GlobalChat {} with id = {}", globalChat.isPresent() ? "found" : "not found", id);
        return globalChat;
    }

    public GlobalChat save(GlobalChat globalChat) {
        logger.info("GlobalChat = {} has been saved", globalChat);
        return globalChatRepository.save(globalChat);
    }

    /**
     *
     * @param chatId
     * @author ezuykow
     */
    public void deleteById(long chatId) {
        globalChatRepository.deleteById(chatId);
    }
}
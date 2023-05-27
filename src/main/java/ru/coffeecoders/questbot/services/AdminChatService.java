package ru.coffeecoders.questbot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.repositories.AdminChatRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AdminChatService {

    final Logger logger = LoggerFactory.getLogger(AdminChatService.class);

    private final AdminChatRepository repository;

    public AdminChatService(AdminChatRepository repository) {
        this.repository = repository;
    }

    /**
     *
     * @return
     *@author Anatoliy Shikin
     */
    public List<AdminChat> findAll() {
        List<AdminChat> list = repository.findAll();
        logger.info("AdminChats are {} displaying", list.isEmpty() ? "not" : "");
        return list;
    }

    /**
     *
     * @param id
     * @return
     *@author Anatoliy Shikin
     */
    public Optional<AdminChat> findById(long id) {
        Optional<AdminChat> optional = repository.findById(id);
        logger.info("AdminChat with id = {} {} found", id, optional.isPresent() ? "" : "not");
        return optional;
    }

    /**
     *
     * @param adminChat
     * @return
     *@author Anatoliy Shikin
     */
    public AdminChat save(AdminChat adminChat) {
        logger.info("AdminChat = {} has been saved", adminChat);
        return repository.save(adminChat);
    }

    /**
     * Удаляет админский чат с БД
     * @param adminChat удаляемый чат
     * @author ezuykow
     */
    public void delete(AdminChat adminChat) {
        logger.info("AdminChat = {} has been deleted", adminChat);
        repository.delete(adminChat);
    }

    /**
     *
     * @param chatId
     * @author ezuykow
     */
    public void deleteByChatId(long chatId) {
        logger.info("AdminChat with chatId = {} has been deleted", chatId);
        repository.deleteById(chatId);
    }
}
package ru.coffeecoders.questbot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.entities.AdminChatMembers;
import ru.coffeecoders.questbot.repositories.AdminChatMembersRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author ezuykow
 */
@Service
public class AdminChatMembersService {

    final Logger logger = LoggerFactory.getLogger(AdminChatMembersService.class);

    private final AdminChatMembersRepository repository;

    public AdminChatMembersService(AdminChatMembersRepository repository) {
        this.repository = repository;
    }

    /**
     * @return
     * @author ezuykow
     */
    public List<AdminChatMembers> findAll() {
        List<AdminChatMembers> list = repository.findAll();
        logger.info("AdminChatMembers are {} displaying", list.isEmpty() ? "not" : "");
        return list;
    }

    /**
     * @param chatId
     * @return
     * @author ezuykow
     */
    public Optional<AdminChatMembers> findByChatId(long chatId) {
        Optional<AdminChatMembers> optional = repository.findById(chatId);
        logger.info("AdminChatMember with id = {} {} found", chatId, optional.isPresent() ? "" : "not");
        return optional;
    }

    /**
     * @param adminChatMembers
     * @author ezuykow
     */
    public void save(AdminChatMembers adminChatMembers) {
        logger.info("AdminChatMember = {} has been saved", adminChatMembers);
        repository.save(adminChatMembers);
    }

    /**
     * @param chatId
     * @author ezuykow
     */
    public void deleteByChatId(long chatId) {
        logger.info("AdminChatMember with chatId = {} has been deleted", chatId);
        repository.deleteById(chatId);
    }
}
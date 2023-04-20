package ru.coffeecoders.questbot.services;

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

    private final AdminChatMembersRepository repository;

    public AdminChatMembersService(AdminChatMembersRepository repository) {
        this.repository = repository;
    }

    /**
     *
     * @return
     * @author ezuykow
     */
    public List<AdminChatMembers> findAll() {
        return repository.findAll();
    }

    /**
     *
     * @param chatId
     * @return
     * @author ezuykow
     */
    public Optional<AdminChatMembers> findByChatId(long chatId) {
        return repository.findById(chatId);
    }

    /**
     *
     * @param adminChatMembers
     * @author ezuykow
     */
    public void save(AdminChatMembers adminChatMembers) {
        repository.save(adminChatMembers);
    }

    /**
     *
     * @param chatId
     * @author ezuykow
     */
    public void deleteByChatId(long chatId) {
        repository.deleteById(chatId);
    }
}

package ru.coffeecoders.questbot.services;

import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.repositories.AdminChatRepository;

@Service
public class AdminChatService {
    private final AdminChatRepository adminChatRepository;

    public AdminChatService(AdminChatRepository adminChatRepository) {
        this.adminChatRepository = adminChatRepository;
    }
}

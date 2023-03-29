package ru.coffeecoders.questbot.services;

import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.repositories.GlobalChatRepository;

@Service
public class GlobalChatService {
    private final GlobalChatRepository globalChatRepository;

    public GlobalChatService(GlobalChatRepository globalChatRepository) {
        this.globalChatRepository = globalChatRepository;
    }
}

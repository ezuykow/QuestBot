package ru.coffeecoders.questbot.services;

import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.entities.NewGameCreatingState;
import ru.coffeecoders.questbot.repositories.NewGameCreatingStateRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author ezuykow
 */
@Service
public class NewGameCreatingStateService {

    private final NewGameCreatingStateRepository repository;

    public NewGameCreatingStateService(NewGameCreatingStateRepository repository) {
        this.repository = repository;
    }

    public void save(NewGameCreatingState state) {
        repository.save(state);
    }

    public List<NewGameCreatingState> findAll() {
        return repository.findAll();
    }

    public Optional<NewGameCreatingState> findById(long initiatorChatId) {
        return repository.findById(initiatorChatId);
    }

    public void deleteById(long initiatorChatId) {
        repository.deleteById(initiatorChatId);
    }
}

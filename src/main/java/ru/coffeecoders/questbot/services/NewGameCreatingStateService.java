package ru.coffeecoders.questbot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    final Logger logger = LoggerFactory.getLogger(NewGameCreatingStateService.class);

    private final NewGameCreatingStateRepository repository;

    public NewGameCreatingStateService(NewGameCreatingStateRepository repository) {
        this.repository = repository;
    }

    public void save(NewGameCreatingState state) {
        logger.info("NewGameCreatingState with state = {} has been saved", state);
        repository.save(state);
    }

    public List<NewGameCreatingState> findAll() {
        List<NewGameCreatingState> list = repository.findAll();
        logger.info("List<NewGameCreatingState> are {} displaying", list.isEmpty() ? "not" : "");
        return list;
    }

    public Optional<NewGameCreatingState> findById(long initiatorChatId) {
        Optional<NewGameCreatingState> optional = repository.findById(initiatorChatId);
        logger.info("NewGameCreatingState with initiatorChatId = {} {} found", initiatorChatId, optional.isPresent() ? "" : "not");
        return optional;
    }

    public void delete(NewGameCreatingState state) {
        logger.info("NewGameCreatingState with state = {} has been deleted", state);
        repository.delete(state);
    }
}

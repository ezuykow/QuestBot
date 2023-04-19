package ru.coffeecoders.questbot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.repositories.GameRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    Logger logger = LoggerFactory.getLogger(GameService.class);

    private final GameRepository repository;

    public GameService(GameRepository repository) {
        this.repository = repository;
    }

    public List<Game> findAll() {
        List<Game> list = repository.findAll();
        logger.info("Games {} displaying", list.isEmpty() ? "are not" : "are");
        return list;
    }

    public Optional<Game> findByName(String gameName) {
        Optional<Game> optional = repository.findById(gameName);
        logger.info("Game {} with id = {}", optional.isPresent() ? "found" : "not found", gameName);
        return optional;
    }

    public Game save(Game game) {
        logger.info("Game = {} has been saved", game);
        return repository.save(game);
    }

    public Optional<Game> findByChatId(long chatId) {
        Optional<Game> optional = repository.findByGlobalChatId(chatId);
        logger.info("Game with id = {} {} found", chatId, optional.isPresent() ? "" : "not");
        return optional;
    }
}
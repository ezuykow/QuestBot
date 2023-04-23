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

    /**
     *
     * @return
     *@author Anatoliy Shikin
     */
    public List<Game> findAll() {
        List<Game> list = repository.findAll();
        logger.info("Games = {} are {} displaying",list, list.isEmpty() ? "not" : "");
        return list;
    }

    /**
     *
     * @param gameName
     * @return
     *@author Anatoliy Shikin
     */
    public Optional<Game> findByName(String gameName) {
        Optional<Game> game = repository.findById(gameName);
        logger.info("Game with name = {} {} found", gameName, game.isPresent() ? "" : "not");
        return game;
    }

    /**
     *
     * @param game
     * @return
     *@author Anatoliy Shikin
     */
    public Game save(Game game) {
        logger.info("Game = {} has been saved", game);
        return repository.save(game);
    }
}
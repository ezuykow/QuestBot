package ru.coffeecoders.questbot.services;

import jakarta.transaction.Transactional;
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

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    /**
     *
     * @return
     *@author Anatoliy Shikin
     */
    public List<Game> findAll() {
        List<Game> list = gameRepository.findAll();
        logger.info("Games {} displaying", list.isEmpty() ? "are not" : "are");
        return list;
    }

    /**
     *
     * @param gameName
     * @return
     *@author Anatoliy Shikin
     */
    public Optional<Game> findByName(String gameName) {
        Optional<Game> game = gameRepository.findById(gameName);
        logger.info("Game {} with id = {}", game.isPresent() ? "found" : "not found", gameName);
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
        return gameRepository.save(game);
    }

    /**
     * @author ezuykow
     */
    @Transactional
    public void deleteByGameName(String gameName) {
        gameRepository.deleteByGameName(gameName);
    }
}
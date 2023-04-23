package ru.coffeecoders.questbot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.entities.Player;
import ru.coffeecoders.questbot.repositories.PlayerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    Logger logger = LoggerFactory.getLogger(PlayerService.class);

    private final PlayerRepository repository;

    public PlayerService(PlayerRepository repository) {
        this.repository = repository;
    }

    public List<Player> findAll() {
        List<Player> list = repository.findAll();
        logger.info("Players are {} displaying", list.isEmpty() ? "not" : "");
        return list;
    }

    public Optional<Player> findById(long id) {
        Optional<Player> optional = repository.findById(id);
        logger.info("Player with id = {} {} found", id, optional.isPresent() ? "" : "not");
        return optional;
    }

    public Player save(Player player) {
        logger.info("Player = {} has been saved", player);
        return repository.save(player);
    }
}
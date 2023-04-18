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
    private final PlayerRepository playerRepository;
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> findAll() {
        List<Player> list = playerRepository.findAll();
        logger.info("Players {} displaying", list.isEmpty() ? "are not" : "are");
        return list;
    }

    public Optional<Player> findById(long id) {
        Optional<Player> player = playerRepository.findById(id);
        logger.info("Player {} with id = {}", player.isPresent() ? "found" : "not found", id);
        return player;
    }

    public Player save(Player player) {
        logger.info("Player = {} has been saved", player);
        return playerRepository.save(player);
    }
}
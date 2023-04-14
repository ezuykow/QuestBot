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
        if (!list.isEmpty()) {
            logger.info("Players are displaying");
        } else {
            logger.warn("No players found");
        }
        return list;
    }

    public Optional<Player> findById(Long id) {
        Optional<Player> player = playerRepository.findByTgUserId(id);
        if (player.isPresent()) {
            logger.info("Player found with id = {}", id);
        } else {
            logger.warn("Player not found with id = {}", id);
        }
        return player;
    }

    public Player save(Player player) {
        logger.info("Player = {} has been saved", player);
        return playerRepository.save(player);
    }
}

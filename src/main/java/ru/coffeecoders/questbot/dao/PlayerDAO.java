package ru.coffeecoders.questbot.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.models.Player;

import java.util.List;

@Component
public class PlayerDAO {

    private final JdbcTemplate jdbcTemplate;

    public PlayerDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Player> findAll() {
        return null;
    }
}

package ru.coffeecoders.questbot.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.dao.mappers.PlayerMapper;
import ru.coffeecoders.questbot.models.Player;

import java.util.List;

@Component
public class PlayerDAO {

    private final JdbcTemplate jdbcTemplate;
    private final PlayerMapper playerMapper;

    @Value("${dao.player.statement.findAll}")
    private String findAllPreparedStatement;

    public PlayerDAO(JdbcTemplate jdbcTemplate, PlayerMapper playerMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.playerMapper = playerMapper;
    }

    public List<Player> findAll(String tableName) {
        return jdbcTemplate.query(findAllPreparedStatement,
                playerMapper,
                tableName);
    }
}

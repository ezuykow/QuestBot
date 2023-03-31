package ru.coffeecoders.questbot.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.dao.mappers.PlayerMapper;
import ru.coffeecoders.questbot.models.Player;

import java.util.List;
import java.util.Optional;

@Component
public class PlayerDAO {

    @Value("${dynamic.tables.players.idColumn}")
    private String idColumnName;

    private final JdbcTemplate jdbcTemplate;
    private final PlayerMapper playerMapper;

    public PlayerDAO(JdbcTemplate jdbcTemplate, PlayerMapper playerMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.playerMapper = playerMapper;
    }

    public List<Player> findAll(String tableName) {
        final String statement = String.format("SELECT * FROM %s", tableName);
        return jdbcTemplate.query(statement, playerMapper);
    }

    public Optional<Player> findById(String tableName, long id) {
        final String statement = String.format("SELECT * FROM %s WHERE %s = %d",
                tableName, idColumnName, id);
        return jdbcTemplate.query(statement, playerMapper).stream().findAny();
    }

    public boolean save(String tableName, Player player) {
        final String statement = String.format("INSERT INTO %s VALUES(%d, %s)",
                tableName, player.getTgUserId(), player.getTeamName());
        return jdbcTemplate.update(statement) > 0;
    }
}

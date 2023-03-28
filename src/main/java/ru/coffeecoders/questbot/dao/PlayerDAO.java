package ru.coffeecoders.questbot.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.dao.sql.prepared.statements.PlayerPreparedStatements;
import ru.coffeecoders.questbot.models.Player;

import java.util.List;

@Component
public class PlayerDAO {

    private final JdbcTemplate jdbcTemplate;

    private final PlayerPreparedStatements preparedStatements;

    public PlayerDAO(JdbcTemplate jdbcTemplate, PlayerPreparedStatements preparedStatements) {
        this.jdbcTemplate = jdbcTemplate;
        this.preparedStatements = preparedStatements;
    }

    public List<Player> findAll(String tableName) {
        return jdbcTemplate.query(preparedStatements.getFindAllStatement(),
                new Object[]{tableName},
                );
    }
}

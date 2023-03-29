package ru.coffeecoders.questbot.dynamic.tables.creators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PlayersTableCreator {

    @Value("${dynamic.tables.createStatementPrefix}")
    private String createTablePrefix;
    @Value("${dynamic.tables.players.createStatementPostfix}")
    private String createPlayersTableStatementPostfix;

    private final JdbcTemplate jdbcTemplate;

    public PlayersTableCreator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTable(String tableName) {
        final String statement = createTablePrefix + tableName + createPlayersTableStatementPostfix;
        jdbcTemplate.update(statement);
    }
}

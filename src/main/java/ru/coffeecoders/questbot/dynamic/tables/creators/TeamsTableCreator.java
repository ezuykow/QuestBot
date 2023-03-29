package ru.coffeecoders.questbot.dynamic.tables.creators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TeamsTableCreator {

    @Value("${dynamic.tables.createStatementPrefix}")
    private String createTablePrefix;
    @Value("${dynamic.tables.teams.createStatementPostfix}")
    private String createTeamsTableStatementPostfix;

    private final JdbcTemplate jdbcTemplate;

    public TeamsTableCreator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTable(String tableName) {
        final String statement = createTablePrefix + tableName + createTeamsTableStatementPostfix;
        jdbcTemplate.update(statement);
    }
}

package ru.coffeecoders.questbot.dynamic.tables.creators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author ezuykow
 */
@Component
public class TeamsTableCreator {

    @Value("${dynamic.tables.teams.idColumn}")
    private String idColumnName;
    @Value("${dynamic.tables.teams.scoreColumn}")
    private String scoreColumnName;

    private final JdbcTemplate jdbcTemplate;

    public TeamsTableCreator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Создает в БД таблицу команд с названием {@code tableName}
     * @param tableName имя создаваемой таблицы
     */
    public void createTable(String tableName) {
        final String statement = String.format("CREATE TABLE %s (" +
                        "%s VARCHAR(100) PRIMARY KEY ," +
                        "%s INT NOT NULL DEFAULT (0)" +
                        ")",
                tableName, idColumnName, scoreColumnName);
        jdbcTemplate.update(statement);
    }
}

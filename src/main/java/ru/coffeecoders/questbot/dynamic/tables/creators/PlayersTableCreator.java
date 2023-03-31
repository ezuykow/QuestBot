package ru.coffeecoders.questbot.dynamic.tables.creators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author ezuykow
 */
@Component
public class PlayersTableCreator {

    @Value("${dynamic.tables.players.idColumn}")
    private String idColumnName;
    @Value("${dynamic.tables.players.teamNameColumn}")
    private String teamNameColumnName;

    private final JdbcTemplate jdbcTemplate;

    public PlayersTableCreator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Создает в БД таблицу игроков с названием {@code tableName}
     * @param tableName имя создаваемой таблицы
     */
    public void createTable(String tableName) {
        final String statement = String.format("CREATE TABLE %s (" +
                        "%s BIGINT PRIMARY KEY ," +
                        "%s VARCHAR(100) NOT NULL" +
                        ")",
                tableName, idColumnName, teamNameColumnName);
        jdbcTemplate.update(statement);
    }
}

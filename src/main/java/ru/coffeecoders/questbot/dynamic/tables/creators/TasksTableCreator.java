package ru.coffeecoders.questbot.dynamic.tables.creators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author ezuykow
 */
@Component
public class TasksTableCreator {

    @Value("${dynamic.tables.tasks.idColumn}")
    private String idColumnName;
    @Value("${dynamic.tables.tasks.questionIdColumn}")
    private String questionIdColumnName;
    @Value("${dynamic.tables.tasks.performedTeamNameColumn}")
    private String performedTeamNameColumnName;

    private final JdbcTemplate jdbcTemplate;

    public TasksTableCreator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Создает в БД таблицу задач с названием {@code tableName}
     * @param tableName имя создаваемой таблицы
     */
    public void createTable(String tableName) {
        final String statement = String.format("CREATE TABLE %s (" +
                        "%s SERIAL PRIMARY KEY ," +
                        "%s BIGINT UNIQUE NOT NULL ," +
                        "%s VARCHAR(100)" +
                        ")",
                tableName, idColumnName, questionIdColumnName, performedTeamNameColumnName);
        jdbcTemplate.update(statement);
    }
}

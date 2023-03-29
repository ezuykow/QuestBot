package ru.coffeecoders.questbot.dynamic.tables.creators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TasksTableCreator {

    @Value("${dynamic.tables.createStatementPrefix}")
    private String createTablePrefix;
    @Value("${dynamic.tables.tasks.createStatementPostfix}")
    private String createTasksTablePreparedStatementPostfix;

    private final JdbcTemplate jdbcTemplate;

    public TasksTableCreator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTable(String tableName) {
        final String statement = createTablePrefix + tableName + createTasksTablePreparedStatementPostfix;
        jdbcTemplate.update(statement);
    }
}

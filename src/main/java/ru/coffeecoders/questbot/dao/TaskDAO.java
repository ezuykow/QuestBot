package ru.coffeecoders.questbot.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.dao.mappers.TaskMapper;
import ru.coffeecoders.questbot.models.Task;

import java.util.List;

@Component
public class TaskDAO {

    private final JdbcTemplate jdbcTemplate;
    private final TaskMapper taskMapper;

    public TaskDAO(JdbcTemplate jdbcTemplate, TaskMapper taskMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.taskMapper = taskMapper;
    }

    public List<Task> findAll(String tableName) {
        final String statement = String.format("SELECT * FROM %s", tableName);
        return jdbcTemplate.query(statement, taskMapper);
    }
}

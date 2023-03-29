package ru.coffeecoders.questbot.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.dao.mappers.TaskMapper;
import ru.coffeecoders.questbot.models.Task;

import java.util.List;

@Component
public class TaskDAO {

    private final JdbcTemplate jdbcTemplate;
    private final TaskMapper taskMapper;

    @Value("${dao.statements.findAll}")
    private String findAllPreparedStatement;

    public TaskDAO(JdbcTemplate jdbcTemplate, TaskMapper taskMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.taskMapper = taskMapper;
    }

    public List<Task> findAll(String tableName) {
        return jdbcTemplate.query(findAllPreparedStatement,
                taskMapper,
                tableName);
    }
}

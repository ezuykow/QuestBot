package ru.coffeecoders.questbot.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author ezuykow
 */
@Component
public class TaskDAO {
/*
    private static class TaskMapper implements RowMapper<Task> {

        @Value("${dynamic.tables.tasks.idColumn}")
        private String taskIdColumnName;
        @Value("${dynamic.tables.tasks.questionIdColumn}")
        private String questionIdColumnName;
        @Value("${dynamic.tables.tasks.performedTeamNameColumn}")
        private String performedTeamNameColumnName;

        @Override
        public Task mapRow(ResultSet rs, int rowNum) throws SQLException {

            Task task = new Task();
            task.setTaskId(rs.getInt(taskIdColumnName));
            task.setQuestionId(rs.getLong(questionIdColumnName));
            task.setPerformedTeamName(rs.getString(performedTeamNameColumnName));

            return task;
        }
    }

    private final JdbcTemplate jdbcTemplate;
    private final TaskMapper taskMapper;

    public TaskDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.taskMapper = new TaskMapper();
    }

    *//**
     * Поиск всех задач
     * @param tableName имя таблицы, из которой брать задачи
     * @return список задач
     *//*
    public List<Task> findAll(String tableName) {
        final String statement = String.format("SELECT * FROM %s", tableName);
        return jdbcTemplate.query(statement, taskMapper);
    }*/
}

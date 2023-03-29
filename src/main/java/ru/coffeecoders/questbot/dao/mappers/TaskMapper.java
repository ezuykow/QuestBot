package ru.coffeecoders.questbot.dao.mappers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.models.Task;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TaskMapper implements RowMapper<Task> {

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

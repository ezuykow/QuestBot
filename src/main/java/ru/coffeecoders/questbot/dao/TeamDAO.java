package ru.coffeecoders.questbot.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.dao.mappers.TeamMapper;
import ru.coffeecoders.questbot.models.Team;

import java.util.List;

@Component
public class TeamDAO {

    private final JdbcTemplate jdbcTemplate;
    private final TeamMapper teamMapper;

    @Value("${dao.statements.findAll}")
    private String findAllPreparedStatement;

    public TeamDAO(JdbcTemplate jdbcTemplate, TeamMapper teamMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.teamMapper = teamMapper;
    }

    public List<Team> findAll(String tableName) {
        return jdbcTemplate.query(findAllPreparedStatement,
                teamMapper,
                tableName);
    }
}

package ru.coffeecoders.questbot.dao.mappers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.models.Team;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TeamMapper implements RowMapper<Team> {

    @Value("${dynamic.tables.teams.idColumn}")
    private String teamNameColumnName;
    @Value("${dynamic.tables.teams.scoreColumn}")
    private String scoreColumnName;

    @Override
    public Team mapRow(ResultSet rs, int rowNum) throws SQLException {

        Team team = new Team();
        team.setTeamName(rs.getString(teamNameColumnName));
        team.setScore(rs.getInt(scoreColumnName));

        return team;
    }
}

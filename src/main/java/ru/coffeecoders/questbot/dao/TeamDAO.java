package ru.coffeecoders.questbot.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.models.Team;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author ezuykow
 */
@Component
public class TeamDAO {

    private static class TeamMapper implements RowMapper<Team> {

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

    private final JdbcTemplate jdbcTemplate;
    private final TeamMapper teamMapper;

    public TeamDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.teamMapper = new TeamMapper();
    }

    /**
     * Поиск всех команд
     * @param tableName имя таблицы, из которой брать команды
     * @return список команд
     */
    public List<Team> findAll(String tableName) {
        final String statement = String.format("SELECT * FROM %s", tableName);
        return jdbcTemplate.query(statement, teamMapper);
    }
}

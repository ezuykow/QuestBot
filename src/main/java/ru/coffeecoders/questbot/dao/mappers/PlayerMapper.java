package ru.coffeecoders.questbot.dao.mappers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.models.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PlayerMapper implements RowMapper<Player> {

    @Value("${dynamic.tables.players.idColumn}")
    private String tgUserIdColumnName;
    @Value("${dynamic.tables.players.teamNameColumn}")
    private String teamNameColumnName;

    @Override
    public Player mapRow(ResultSet rs, int rowNum) throws SQLException {

        Player player = new Player();
        player.setTgUserId(rs.getLong(tgUserIdColumnName));
        player.setTeamName(rs.getString(teamNameColumnName));

        return player;
    }
}

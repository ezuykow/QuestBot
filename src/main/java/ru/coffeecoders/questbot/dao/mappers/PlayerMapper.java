package ru.coffeecoders.questbot.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.coffeecoders.questbot.models.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerMapper implements RowMapper<Player> {

    @Override
    public Player mapRow(ResultSet rs, int rowNum) throws SQLException {

        Player player = new Player();
        player.setTgUserId(rs.getLong("tg_user_id"));

        return null;
    }
}

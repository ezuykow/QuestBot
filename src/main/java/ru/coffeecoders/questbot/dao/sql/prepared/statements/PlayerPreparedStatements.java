package ru.coffeecoders.questbot.dao.sql.prepared.statements;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "player.dao")
public class PlayerPreparedStatements {

    private String findAll;

    public String getFindAllStatement() {
        return findAll;
    }
}

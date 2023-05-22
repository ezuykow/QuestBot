package ru.coffeecoders.questbot.configs;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.coffeecoders.questbot.properties.PropertySeed;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ezuykow
 */
@Configuration
public class QuestBotAppConfig {

    @Value("${telegram.bot.token}")
    private String token;
    @Value("${properties.count}")
    private int propertiesCount;

    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(token);
    }

    @Bean
    public Map<String, PropertySeed> properties() {
        return new HashMap<>(propertiesCount);
    }
}

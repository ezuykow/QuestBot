package ru.coffeecoders.questbot.models;

import com.pengrad.telegrambot.model.Update;

import java.util.Optional;

/**
 * @author ezuykow
 */
public class ExtendedUpdate extends Update {

    private static final String TEXT_COMMAND_REGEXP = "/.+";

    /**
     * @return {@code true}, если апдейт содержит {@code message().text()},
     * {@code false} - в противном случае
     */
    public boolean hasMessageText() {
        return tryToGetMessageText().isPresent();
    }

    /**
     * @return {@code true}, если апдейт является командой (т.е. начинается с "/"),
     * {@code false} в противном случае
     */
    public boolean isCommand() {
        return hasMessageText() && message().text().matches(TEXT_COMMAND_REGEXP);
    }

    private Optional<String> tryToGetMessageText() {
        try {
            return Optional.of(message().text());
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }
}

package ru.coffeecoders.questbot.models;

import com.pengrad.telegrambot.model.*;

import java.util.Optional;

/**
 * @author ezuykow
 */
public class ExtendedUpdate{

    private static final String TEXT_COMMAND_REGEXP = "/.+";

    public enum UpdateType {
        COMMAND,
        DOCUMENT,
        UNKNOWN
    }

    private Update update;

    public ExtendedUpdate(Update update) {
        this.update = update;
    }

    /**
     * @return {@code true}, если апдейт содержит {@code message()},
     * {@code false} - в противном случае
     */
    public boolean hasMessage() {
        return tryToGetMessage().isPresent();
    }

    /**
     * @return {@code true}, если апдейт содержит {@code message().text()},
     * {@code false} - в противном случае
     */
    public boolean hasMessageText() {
        return tryToGetMessageText().isPresent();
    }

    /**
     * @return {@code true}, если апдейт содержит {@code message().document()},
     *      * {@code false} - в противном случае
     */
    public boolean hasDocument() {
        return tryToGetDocument().isPresent();
    }

    /**
     * @return {@code true}, если апдейт является командой (т.е. начинается с "/"),
     * {@code false} в противном случае
     */
    public boolean isCommand() {
        return hasMessageText() && update.message().text().matches(TEXT_COMMAND_REGEXP);
    }

    /**
     * @return тип апдейта - объект {@link UpdateType}
     */
    public UpdateType getUpdateType() {
        if (isCommand()) {
            return UpdateType.COMMAND;
        }
        if (hasDocument()) {
            return UpdateType.DOCUMENT;
        }
        return UpdateType.UNKNOWN;
    }

    /**
     * @return id чата, с которого пришел апдейт <br>
     * Использя {@link Update#message()} <br>
     * {@link Message#chat()} <br>
     * {@link Chat#id()}
     */
    public long getMessageChatId() {
        if (hasMessage()) {
            return update.message().chat().id();
        }
        throw new RuntimeException("Update haven't message!");
    }

    /**
     * @return document из апдейта <br>
     * Использя {@link Update#message()} <br>
     * {@link Message#document()}
     */
    public Document getDocument() {
        if (hasDocument()) {
            return update.message().document();
        }
        throw new RuntimeException("Update haven't document!");
    }

    /**
     * @return text из message из апдейта <br>
     * Использя {@link Update#message()} <br>
     * {@link Message#text()}
     */
    public String getMessageText() {
        if (hasMessageText()) {
            return update.message().text();
        }
        throw new RuntimeException("Update haven't message text!");
    }

    /**
     * @return id юзера, от которого пришел апдейт <br>
     * Использя {@link Update#message()} <br>
     * {@link Message#from()} <br>
     * {@link User#id()}
     */
    public long getMessageFromUserId() {
        if (hasMessage()) {
            return update.message().from().id();
        }
        throw new RuntimeException("Update haven't message");
    }

    private Optional<Message> tryToGetMessage() {
        try {
            return Optional.of(update.message());
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    private Optional<String> tryToGetMessageText() {
        try {
            return Optional.of(update.message().text());
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    private Optional<Document> tryToGetDocument() {
        try {
            return Optional.of(update.message().document());
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }
}

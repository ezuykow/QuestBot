package ru.coffeecoders.questbot.models;

import com.pengrad.telegrambot.model.*;

import java.util.Optional;

import static ru.coffeecoders.questbot.models.ExtendedUpdate.UpdateType.*;

/**
 * @author ezuykow
 */
public class ExtendedUpdate{

    private static final String TEXT_COMMAND_REGEXP = "/.+";

    public enum UpdateType {
        SIMPLE_MESSAGE,
        COMMAND,
        DOCUMENT,
        CALLBACK,
        NEW_CHAT_MEMBER,
        LEFT_CHAT_MEMBER,
        NEW_OR_LEFT_CHAT_MEMBERS_MESSAGE,
        UNKNOWN
    }

    public final Update update;

    public ExtendedUpdate(Update update) {
        this.update = update;
    }

    /**
     * @return {@code true}, если апдейт содержит {@code message()},
     * {@code false} - в противном случае
     */
    public boolean hasMessage() {
        return getMessageOpt().isPresent();
    }

    /**
     * @return {@code true}, если апдейт содержит {@code message().text()},
     * {@code false} - в противном случае
     */
    public boolean hasMessageText() {
        return hasMessage() && getMessageTextOpt().isPresent();
    }

    /**
     * @return {@code true}, если апдейт содержит {@code message().document()},
     *      * {@code false} - в противном случае
     */
    public boolean hasDocument() {
        return hasMessage() && getDocumentOpt().isPresent();
    }

    /**
     * @return {@code true}, если апдейт содержит {@code callbackQuery()},
     *      * {@code false} - в противном случае
     */
    public boolean hasCallbackQuery() {
        return getCallbackQueryOpt().isPresent();
    }

    /**
     * @author ezuykow
     */
    public boolean hasChatMemberUpdated() {
        return update.chatMember() != null;
    }

    /**
     *
     * @return вав
     * @author ezuykow
     */
    public boolean hasReplyToMessage() {
        return update.message().replyToMessage() != null;
    }

    /**
     * @return {@code true}, если апдейт является командой (т.е. начинается с "/"),
     * {@code false} в противном случае
     */
    public boolean isCommand() {
        return hasMessageText() && update.message().text().matches(TEXT_COMMAND_REGEXP);
    }

    /**
     * @author ezuykow
     */
    public boolean isMemberJoin() {
        if (hasChatMemberUpdated()) {
            return update.chatMember().oldChatMember().status().equals(ChatMember.Status.left)
                    && update.chatMember().newChatMember().status().equals(ChatMember.Status.member);
        }
        return false;
    }

    /**
     * @author ezuykow
     */
    public boolean isMemberLeft() {
        if (hasChatMemberUpdated()) {
            return update.chatMember().oldChatMember().status().equals(ChatMember.Status.member)
                    && update.chatMember().newChatMember().status().equals(ChatMember.Status.left);
        }
        return false;
    }

    /**
     *
     * @return ава
     * @author ezuykow
     */
    public Message getReplyToMessage() {
        if (hasReplyToMessage()) {
            return update.message().replyToMessage();
        }
        throw new RuntimeException("Update haven't replyToMessage!");
    }

    /**
     * @return тип апдейта - объект {@link UpdateType}
     */
    public UpdateType getUpdateType() {
        if (isCommand()) {
            return COMMAND;
        }
        if (messageWithNewOrLeftChatMember()) {
            return NEW_OR_LEFT_CHAT_MEMBERS_MESSAGE;
        }
        if (isMemberJoin()) {
            return NEW_CHAT_MEMBER;
        }
        if (isMemberLeft()) {
            return LEFT_CHAT_MEMBER;
        }
        if (hasDocument()) {
            return DOCUMENT;
        }
        if (hasCallbackQuery()) {
            return CALLBACK;
        }
        if (hasMessageText()) {
            return SIMPLE_MESSAGE;
        }
        return UNKNOWN;
    }

    /**
     * @return id чата, с которого пришел апдейт <br>
     * Используя {@link Update#message()} <br>
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
     * Используя {@link Update#message()} <br>
     * {@link Message#document()}
     */
    public Document getDocument() {
        if (hasDocument()) {
            return update.message().document();
        }
        throw new RuntimeException("Update haven't document!");
    }

    /**
     *
     * @return ваа
     * @author ezuykow
     */
    public int getMessageId() {
        if (hasMessage()) {
            return update.message().messageId();
        }
        throw new RuntimeException("Update haven't message!");
    }

    /**
     *
     * @return ава
     * @author ezuykow
     */
    public String getUsernameFromMessage() {
        if (hasMessage()) {
            return update.message().from().username();
        }
        throw new RuntimeException("Update haven't message!");
    }

    /**
     * @return text из message из апдейта <br>
     * Используя {@link Update#message()} <br>
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
     * Используя {@link Update#message()} <br>
     * {@link Message#from()} <br>
     * {@link User#id()}
     */
    public long getMessageFromUserId() {
        if (hasMessage()) {
            return update.message().from().id();
        }
        throw new RuntimeException("Update haven't message");
    }

    public String getCallbackQueryId() {
        if (hasCallbackQuery()) {
            return update.callbackQuery().id();
        }
        throw new RuntimeException("Update haven't callbackQuery!");
    }

    /**
     * @return данные из CallbackQuery<br>
     * Используя {@link Update#callbackQuery()} <br>
     * {@link CallbackQuery#data()}
     */
    public String getCallbackQueryData() {
        if (hasCallbackQuery()) {
            return update.callbackQuery().data();
        }
        throw new RuntimeException("Update haven't callbackQuery!");
    }

    /**
     * @return id чата, с которого пришел калбак <br>
     * Используя {@link Update#callbackQuery()} <br>
     * {@link CallbackQuery#message()} <br>
     * {@link Message#chat()} <br>
     * {@link Chat#id()}
     */
    public long getCallbackMessageChatId() {
        if (hasCallbackQuery()) {
            return update.callbackQuery().message().chat().id();
        }
        throw new RuntimeException("Update haven't callbackQuery!");
    }

    /**
     * @return id сообщения, с которого пришел калбак <br>
     * Используя {@link Update#callbackQuery()} <br>
     * {@link CallbackQuery#message()} <br>
     * {@link Message#messageId()} ()}
     */
    public int getCallbackMessageId() {
        if (hasCallbackQuery()) {
            return update.callbackQuery().message().messageId();
        }
        throw new RuntimeException("Update haven't callbackQuery!");
    }

    /**
     * @author ezuykow
     */
    public long getCallbackFromUserId() {
        if (hasCallbackQuery()) {
            return update.callbackQuery().from().id();
        }
        throw new RuntimeException("Update haven't callbackQuery!");
    }

    /**
     * @author ezuykow
     */
    public User getUpdatedMemberUser() {
        if (hasChatMemberUpdated()) {
            return update.chatMember().newChatMember().user();
        }
        throw new RuntimeException("Update haven't ChatMemberUpdated!");
    }

    /**
     * @author ezuykow
     */
    public long getUpdatedMemberChatId() {
        if (hasChatMemberUpdated()) {
            return update.chatMember().chat().id();
        }
        throw new RuntimeException("Update haven't ChatMemberUpdated!");
    }

    /**
     * @author ezuykow
     */
    private Optional<Message> getMessageOpt() {
        return Optional.ofNullable(update.message());
    }

    /**
     * @author ezuykow
     */
    private Optional<String> getMessageTextOpt() {
        return Optional.ofNullable(update.message().text());
    }

    /**
     * @author ezuykow
     */
    private Optional<Document> getDocumentOpt() {
        return Optional.ofNullable(update.message().document());
    }

    /**
     * @author ezuykow
     */
    private Optional<CallbackQuery> getCallbackQueryOpt() {
        return Optional.ofNullable(update.callbackQuery());
    }

    /**
     * @author ezuykow
     */
    private boolean messageWithNewOrLeftChatMember() {
        return hasMessage() && (update.message().newChatMembers() != null || update.message().leftChatMember() != null);
    }
}

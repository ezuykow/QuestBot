package ru.coffeecoders.questbot.models;

import com.pengrad.telegrambot.model.*;
import ru.coffeecoders.questbot.exceptions.*;

import static ru.coffeecoders.questbot.models.ExtendedUpdate.UpdateType.*;

/**
 * @author ezuykow
 */
public class ExtendedUpdate{

    private static final String TEXT_COMMAND_REGEXP = "/.*";

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

    private final Update update;

    public ExtendedUpdate(Update update) {
        this.update = update;
    }

    //-----------------API START-----------------

    /**
     * @return {@code true}, если апдейт содержит {@code message()},
     * {@code false} - в противном случае
     * @author ezuykow
     */
    public boolean hasMessage() {
        return update.message() != null;
    }

    /**
     * @return {@code true}, если апдейт содержит {@code message().text()},
     * {@code false} - в противном случае
     * @author ezuykow
     */
    public boolean hasMessageText() {
        return hasMessage() && (update.message().text() != null);
    }

    /**
     * @return {@code true}, если апдейт содержит {@code message().document()},
     * {@code false} - в противном случае
     * @author ezuykow
     */
    public boolean hasDocument() {
        return hasMessage() && (update.message().document() != null);
    }

    /**
     * @return {@code true}, если апдейт содержит {@code callbackQuery()},
     * {@code false} - в противном случае
     * @author ezuykow
     */
    public boolean hasCallbackQuery() {
        return update.callbackQuery() != null;
    }

    /**
     * @return true, если апдейт содержит {@link ChatMember}, false - в противном случае
     * @author ezuykow
     */
    public boolean hasChatMemberUpdated() {
        return update.chatMember() != null;
    }

    /**
     * @return true, если апдейт содержит {@code message().replyToMessage()}, false - в противном случае
     * @author ezuykow
     */
    public boolean hasReplyToMessage() {
        return hasMessage() && (update.message().replyToMessage() != null);
    }

    /**
     * @return {@code true}, если апдейт является командой (т.е. начинается с "/"),
     * {@code false} в противном случае
     * @author ezuykow
     */
    public boolean isCommand() {
        return hasMessageText() && update.message().text().matches(TEXT_COMMAND_REGEXP);
    }

    /**
     * @return true, если апдейт содержит информацию о вступлении нового пользователя, false - в противном случае
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
     * @return true, если апдейт содержит информацию о покидании чата пользователем, false - в противном случае
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
     * @return Message, на который ответом пришло сообщение в текущем апдейте
     * @author ezuykow
     */
    public Message getReplyToMessage() {
        if (hasReplyToMessage()) {
            return update.message().replyToMessage();
        }
        throw new NonExistentReplyToMessage();
    }

    /**
     * @return тип апдейта - объект {@link UpdateType}
     * @author ezuykow
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
     * @author ezuykow
     */
    public long getMessageChatId() {
        if (hasMessage()) {
            return update.message().chat().id();
        }
        throw new NonExistentMessage();
    }

    /**
     * @return document из апдейта <br>
     * Используя {@link Update#message()} <br>
     * {@link Message#document()}
     * @author ezuykow
     */
    public Document getDocument() {
        if (hasDocument()) {
            return update.message().document();
        }
        throw new NonExistentDocument();
    }

    /**
     * @return id сообщения
     * @author ezuykow
     */
    public int getMessageId() {
        if (hasMessage()) {
            return update.message().messageId();
        }
        throw new NonExistentMessage();
    }

    /**
     * @return username пользователя, отправившего сообщения
     * @author ezuykow
     */
    public String getUsernameFromMessage() {
        if (hasMessage()) {
            return update.message().from().username();
        }
        throw new NonExistentMessage();
    }

    /**
     * @return text из message из апдейта <br>
     * Используя {@link Update#message()} <br>
     * {@link Message#text()}
     * @author ezuykow
     */
    public String getMessageText() {
        if (hasMessageText()) {
            return update.message().text();
        }
        throw new NonExistentMessage();
    }

    /**
     * @return id юзера, от которого пришел апдейт <br>
     * Используя {@link Update#message()} <br>
     * {@link Message#from()} <br>
     * {@link User#id()}
     * @author ezuykow
     */
    public long getMessageFromUserId() {
        if (hasMessage()) {
            return update.message().from().id();
        }
        throw new NonExistentMessage();
    }

    /**
     * @return id пришедшего CallbackQuery
     * @author ezuykow
     */
    public String getCallbackQueryId() {
        if (hasCallbackQuery()) {
            return update.callbackQuery().id();
        }
        throw new NonExistentCallbackQuery();
    }

    /**
     * @return данные из CallbackQuery<br>
     * Используя {@link Update#callbackQuery()} <br>
     * {@link CallbackQuery#data()}
     * @author ezuykow
     */
    public String getCallbackQueryData() {
        if (hasCallbackQuery()) {
            return update.callbackQuery().data();
        }
        throw new NonExistentCallbackQuery();
    }

    /**
     * @return id чата, с которого пришел калбак <br>
     * Используя {@link Update#callbackQuery()} <br>
     * {@link CallbackQuery#message()} <br>
     * {@link Message#chat()} <br>
     * {@link Chat#id()}
     * @author ezuykow
     */
    public long getCallbackMessageChatId() {
        if (hasCallbackQuery()) {
            return update.callbackQuery().message().chat().id();
        }
        throw new NonExistentCallbackQuery();
    }

    /**
     * @return id сообщения, с которого пришел калбак <br>
     * Используя {@link Update#callbackQuery()} <br>
     * {@link CallbackQuery#message()} <br>
     * {@link Message#messageId()} ()}
     * @author ezuykow
     */
    public int getCallbackMessageId() {
        if (hasCallbackQuery()) {
            return update.callbackQuery().message().messageId();
        }
        throw new NonExistentCallbackQuery();
    }

    /**
     * @return id пользователя, от которого пришел CallbackQuery
     * @author ezuykow
     */
    public long getCallbackFromUserId() {
        if (hasCallbackQuery()) {
            return update.callbackQuery().from().id();
        }
        throw new NonExistentCallbackQuery();
    }

    /**
     * @return пользователя ({@link User}), состояние которого было изменено
     * @author ezuykow
     */
    public User getUpdatedMemberUser() {
        if (hasChatMemberUpdated()) {
            return update.chatMember().newChatMember().user();
        }
        throw new NonExistentChatMemberUpdated();
    }

    /**
     * @return id чата, в котором изменено состояние пользователя
     * @author ezuykow
     */
    public long getUpdatedMemberChatId() {
        if (hasChatMemberUpdated()) {
            return update.chatMember().chat().id();
        }
        throw new NonExistentChatMemberUpdated();
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private boolean messageWithNewOrLeftChatMember() {
        return hasMessage() && (update.message().newChatMembers() != null || update.message().leftChatMember() != null);
    }
}

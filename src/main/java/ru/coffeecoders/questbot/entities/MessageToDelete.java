package ru.coffeecoders.questbot.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

/**
 * @author ezuykow
 */
@Entity
@Table(name = "msg_to_delete")
public class MessageToDelete {

    @Id
    @Column(name = "msg_id")
    int msgId;

    @Column(name = "user_id")
    long userId;

    @Column(name = "relate_to")
    String relate_to;

    @Column(name = "chat_id")
    long chatId;

    @Column(name = "active")
    boolean active;

    public MessageToDelete() {
    }

    public MessageToDelete(int msgId, long userId, String relate_to, long chatId, boolean active) {
        this.msgId = msgId;
        this.userId = userId;
        this.relate_to = relate_to;
        this.chatId = chatId;
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getRelate_to() {
        return relate_to;
    }

    public void setRelate_to(String relate_to) {
        this.relate_to = relate_to;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageToDelete that = (MessageToDelete) o;
        return msgId == that.msgId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(msgId);
    }

    @Override
    public String toString() {
        return "MessageToDelete{" +
                "msgId=" + msgId +
                ", userId=" + userId +
                ", relate_to='" + relate_to + '\'' +
                ", chatId=" + chatId +
                ", active=" + active +
                '}';
    }
}

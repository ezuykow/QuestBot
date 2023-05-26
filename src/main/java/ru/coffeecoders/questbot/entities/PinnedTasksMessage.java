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
@Table(name = "pinned_tasks_messages")
public class PinnedTasksMessage {

    @Id
    @Column(name = "message_id")
    private int msgId;

    @Column(name = "chat_id")
    private long chatId;

    public PinnedTasksMessage() {
    }

    public PinnedTasksMessage(int msgId, long chatId) {
        this.msgId = msgId;
        this.chatId = chatId;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
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
        PinnedTasksMessage that = (PinnedTasksMessage) o;
        return msgId == that.msgId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(msgId);
    }

    @Override
    public String toString() {
        return "PinnedTasksMessage{" +
                "msgId=" + msgId +
                ", chatId=" + chatId +
                '}';
    }
}

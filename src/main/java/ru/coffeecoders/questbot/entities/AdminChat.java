package ru.coffeecoders.questbot.entities;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * @author ezuykow
 */
@Entity
@Table(name = "admin_chats")
public class AdminChat {

    @Id
    @Column(name = "tg_admin_chat_id")
    private long tgAdminChatId;

    public long getTgAdminChatId() {
        return tgAdminChatId;
    }

    public void setTgAdminChatId(long tgAdminChatId) {
        this.tgAdminChatId = tgAdminChatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminChat adminChat = (AdminChat) o;
        return tgAdminChatId == adminChat.tgAdminChatId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tgAdminChatId);
    }

    @Override
    public String toString() {
        return "AdminChat{" +
                "tgAdminChatId=" + tgAdminChatId +
                '}';
    }
}

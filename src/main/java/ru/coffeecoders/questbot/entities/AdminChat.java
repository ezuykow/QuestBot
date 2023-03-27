package ru.coffeecoders.questbot.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "admin_chats")
public class AdminChat {

    @Id
    @Column(name = "admin_chat_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long adminChatId;

    @Column(name = "tg_chat_id")
    private long tgChatId;

    public long getAdminChatId() {
        return adminChatId;
    }

    public void setAdminChatId(long adminChatId) {
        this.adminChatId = adminChatId;
    }

    public long getTgChatId() {
        return tgChatId;
    }

    public void setTgChatId(long tgChatId) {
        this.tgChatId = tgChatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminChat adminChat = (AdminChat) o;
        return adminChatId == adminChat.adminChatId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(adminChatId);
    }

    @Override
    public String toString() {
        return "AdminChat{" +
                "adminChatId=" + adminChatId +
                ", tgChatId=" + tgChatId +
                '}';
    }
}

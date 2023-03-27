package ru.coffeecoders.questbot.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "admins")
public class Admin {

    @Id
    @Column(name = "admin_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admins_admin_id_seq")
    private long id;

    @Column(name = "tg_user_id")
    private long TgUserId;

    @Column(name = "tg_admin_chat_id")
    private long TgAdminChatId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTgUserId() {
        return TgUserId;
    }

    public void setTgUserId(long tgUserId) {
        TgUserId = tgUserId;
    }

    public long getTgAdminChatId() {
        return TgAdminChatId;
    }

    public void setTgAdminChatId(long tgAdminChatId) {
        TgAdminChatId = tgAdminChatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        return id == admin.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", TgUserId=" + TgUserId +
                ", TgAdminChatId=" + TgAdminChatId +
                '}';
    }
}

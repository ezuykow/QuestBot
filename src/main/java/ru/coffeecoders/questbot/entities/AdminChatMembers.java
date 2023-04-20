package ru.coffeecoders.questbot.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author ezuykow
 */
@Entity
@Table(name = "admin_chats_members")
public class AdminChatMembers {

    @Id
    @Column(name = "tg_admin_chat_id")
    private long adminChatId;

    @Column(name = "members")
    private long[] members;

    public AdminChatMembers() {
    }

    public AdminChatMembers(long adminChatId, long[] members) {
        this.adminChatId = adminChatId;
        this.members = members;
    }

    public long getAdminChatId() {
        return adminChatId;
    }

    public void setAdminChatId(long adminChatId) {
        this.adminChatId = adminChatId;
    }

    public long[] getMembers() {
        return members;
    }

    public void setMembers(long[] members) {
        this.members = members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminChatMembers that = (AdminChatMembers) o;
        return adminChatId == that.adminChatId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(adminChatId);
    }

    @Override
    public String toString() {
        return "AdminChatMembers{" +
                "adminChatId=" + adminChatId +
                ", members=" + Arrays.toString(members) +
                '}';
    }
}

package ru.coffeecoders.questbot.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author ezuykow
 */
@Entity
@Table(name = "admin_chats")
public class AdminChat {

    @Id
    @Column(name = "tg_admin_chat_id")
    private long tgAdminChatId;

    @Column(name = "blocked_by_admin_id")
    private long blockedByAdminId;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @JoinTable(name = "admins_admin_chats",
            joinColumns = @JoinColumn(name = "tg_admin_chat_id"),
            inverseJoinColumns = @JoinColumn(name = "tg_admin_user_id")
    )
    private Set<Admin> admins = new HashSet<>();

    public AdminChat() {
    }

    public AdminChat(long tgAdminChatId) {
        this(tgAdminChatId, null, null);
    }

    public AdminChat(long tgAdminChatId, long blockedByAdminId) {
        this(tgAdminChatId, blockedByAdminId, null);
    }

    public AdminChat(long tgAdminChatId, Set<Admin> admins) {
        this(tgAdminChatId, null, admins);
    }

    public AdminChat(long tgAdminChatId, Long blockedByAdminId, Set<Admin> admins) {
        this.tgAdminChatId = tgAdminChatId;
        if (blockedByAdminId != null) {
            this.blockedByAdminId = blockedByAdminId;
        }
        if (admins != null) {
            this.admins = admins;
        }
    }

    public long getTgAdminChatId() {
        return tgAdminChatId;
    }

    public void setTgAdminChatId(long tgAdminChatId) {
        this.tgAdminChatId = tgAdminChatId;
    }

    public long getBlockedByAdminId() {
        return blockedByAdminId;
    }

    public void setBlockedByAdminId(long blockedByAdminId) {
        this.blockedByAdminId = blockedByAdminId;
    }

    public Set<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<Admin> admins) {
        this.admins = admins;
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
                ", blockedByAdminId=" + blockedByAdminId +
                ", admins=" + admins +
                '}';
    }
}

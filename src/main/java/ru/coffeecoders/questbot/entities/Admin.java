package ru.coffeecoders.questbot.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author ezuykow
 */
@Entity
@Table(name = "admins")
public class Admin {

    @Id
    @Column(name = "tg_admin_user_id")
    private long tgAdminUserId;

    @Column(name = "is_owner")
    private boolean isOwner;

    @ManyToMany(mappedBy = "admins")
    private Set<AdminChat> adminChats = new HashSet<>();

    public Admin() {
    }

    public Admin(long tgAdminUserId) {
        this(tgAdminUserId, false, null);
    }

    public Admin(long tgAdminUserId, boolean isOwner) {
        this(tgAdminUserId, isOwner, null);
    }

    public Admin(long tgAdminUserId, boolean isOwner, Set<AdminChat> adminChats) {
        this.tgAdminUserId = tgAdminUserId;
        this.isOwner = isOwner;
        if (adminChats != null) {
            this.adminChats = adminChats;
        }
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public Set<AdminChat> getAdminChats() {
        return adminChats;
    }

    public void setAdminChats(Set<AdminChat> adminChats) {
        this.adminChats = adminChats;
    }

    public long getTgAdminUserId() {
        return tgAdminUserId;
    }

    public void setTgAdminUserId(long tgAdminUserId) {
        this.tgAdminUserId = tgAdminUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        return tgAdminUserId == admin.tgAdminUserId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tgAdminUserId);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "tgAdminUserId=" + tgAdminUserId +
                ", isOwner=" + isOwner +
                '}';
    }
}

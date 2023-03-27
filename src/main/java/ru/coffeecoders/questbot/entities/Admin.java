/**
 * Author: ezuykow
 */

package ru.coffeecoders.questbot.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "admins")
public class Admin {

    @Id
    @Column(name = "admin_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long adminId;

    @Column(name = "tg_user_id")
    private long tgUserId;

    public long getAdminId() {
        return adminId;
    }

    public void setAdminId(long id) {
        this.adminId = id;
    }

    public long getTgUserId() {
        return tgUserId;
    }

    public void setTgUserId(long tgUserId) {
        this.tgUserId = tgUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        return adminId == admin.adminId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(adminId);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + adminId +
                ", tgUserId=" + tgUserId +
                '}';
    }
}

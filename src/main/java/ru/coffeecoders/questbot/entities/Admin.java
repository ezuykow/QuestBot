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
    @Column(name = "tg_admin_user_id")
    private long tgAdminUserId;

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
                '}';
    }
}

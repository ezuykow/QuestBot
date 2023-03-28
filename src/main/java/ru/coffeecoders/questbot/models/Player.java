package ru.coffeecoders.questbot.models;

import java.util.Objects;

public class Player {

    private long tgUserId;
    private String teamName;

    public long getTgUserId() {
        return tgUserId;
    }

    public void setTgUserId(long tgUserId) {
        this.tgUserId = tgUserId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return tgUserId == player.tgUserId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tgUserId);
    }

    @Override
    public String toString() {
        return "Player{" +
                "tgUserId=" + tgUserId +
                ", teamName='" + teamName + '\'' +
                '}';
    }
}

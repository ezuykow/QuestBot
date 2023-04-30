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
@Table(name = "players")
public class Player {

    @Id
    @Column(name = "tg_user_id")
    private long tgUserId;

    @Column(name = "game_name")
    private String gameName;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "chat_id")
    private long chatId;

    public Player() {
    }

    public Player(long tgUserId, String gameName, String teamName, long chatId) {
        this.tgUserId = tgUserId;
        this.gameName = gameName;
        this.teamName = teamName;
        this.chatId = chatId;
    }

    @Override
    public String toString() {
        return "Player{" +
                "tgUserId=" + tgUserId +
                ", gameName='" + gameName + '\'' +
                ", teamName='" + teamName + '\'' +
                ", chatId=" + chatId +
                '}';
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public long getTgUserId() {
        return tgUserId;
    }

    public void setTgUserId(long tgUserId) {
        this.tgUserId = tgUserId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
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

}

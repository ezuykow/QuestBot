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
@Table(name = "global_chats")
public class GlobalChat {

    @Id
    @Column(name = "tg_chat_id")
    private long tgChatId;

    @Column(name = "creating_game_name")
    private String creatingGameName;

    @Column(name = "is_game_started")
    private boolean isGameStarted;

    @Column(name = "minutes_since_start")
    private int minutesSinceStart;

    public GlobalChat() {
    }

    public GlobalChat(long tgChatId) {
        this(tgChatId, null, false, null);
    }

    public GlobalChat(long tgChatId, String creatingGameName, boolean isGameStarted, Integer minutesSinceStart) {
        this.tgChatId = tgChatId;
        this.creatingGameName = creatingGameName;
        this.isGameStarted = isGameStarted;
        if (minutesSinceStart != null) {
            this.minutesSinceStart = minutesSinceStart;
        }
    }

    public long getTgChatId() {
        return tgChatId;
    }

    public void setTgChatId(long tgChatId) {
        this.tgChatId = tgChatId;
    }

    public String getCreatingGameName() {
        return creatingGameName;
    }

    public void setCreatingGameName(String creatingGameName) {
        this.creatingGameName = creatingGameName;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
    }

    public int getMinutesSinceStart() {
        return minutesSinceStart;
    }

    public void setMinutesSinceStart(int minutesSinceStart) {
        this.minutesSinceStart = minutesSinceStart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GlobalChat that = (GlobalChat) o;
        return tgChatId == that.tgChatId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tgChatId);
    }

    @Override
    public String toString() {
        return "GlobalChat{" +
                "tgChatId=" + tgChatId +
                ", creatingGameName='" + creatingGameName + '\'' +
                ", isGameStarted=" + isGameStarted +
                ", minutesSinceStart=" + minutesSinceStart +
                '}';
    }
}

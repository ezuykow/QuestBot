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
@Table(name = "teams")
public class Team {

    @Id
    @Column(name = "team_name")
    private String teamName;

    @Column(name = "game_name")
    private String gameName;

    @Column(name = "score")
    private int score;

    @Column(name = "chat_id")
    private long chatId;

    public Team() {
    }

    public Team(String teamName, String gameName, int score, long chatId) {
        this.teamName = teamName;
        this.gameName = gameName;
        this.score = score;
        this.chatId = chatId;
    }

    @Override
    public String toString() {
        return "Team{" +
                "teamName='" + teamName + '\'' +
                ", gameName='" + gameName + '\'' +
                ", score=" + score +
                ", chatId=" + chatId +
                '}';
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return teamName.equals(team.teamName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamName);
    }

}

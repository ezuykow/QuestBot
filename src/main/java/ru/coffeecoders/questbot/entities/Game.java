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
@Table(name = "games")
public class Game {

    @Id
    @Column(name = "game_name")
    private String gameName;

    @Column(name = "groups_ids")
    private int[] groupsIds;

    @Column(name = "max_time_minutes")
    private int maxTimeMinutes;

    @Column(name = "max_questions_count")
    private int maxQuestionsCount;

    @Column(name = "max_performed_questions_count")
    private int maxPerformedQuestionsCount;

    @Column(name = "min_questions_count_in_game")
    private int minQuestionsCountInGame;

    @Column(name = "questions_count_to_add")
    private int questionsCountToAdd;

    @Column(name = "start_count_tasks")
    private int startCountTasks;

    public Game() {
    }

    public Game(String gameName, int maxTimeMinutes, int maxQuestionsCount, int maxPerformedQuestionsCount,
                int minQuestionsCountInGame, int questionsCountToAdd, int startCountTasks)
    {
        this(gameName, null, maxTimeMinutes, maxQuestionsCount, maxPerformedQuestionsCount,
                minQuestionsCountInGame, questionsCountToAdd, startCountTasks
        );
    }

    public Game(String gameName, int[] groupsIds, int maxTimeMinutes, int maxQuestionsCount,
                int maxPerformedQuestionsCount, int minQuestionsCountInGame, int questionsCountToAdd,
                int startCountTasks)
    {
        this.gameName = gameName;
        this.groupsIds = groupsIds;
        this.maxTimeMinutes = maxTimeMinutes;
        this.maxQuestionsCount = maxQuestionsCount;
        this.maxPerformedQuestionsCount = maxPerformedQuestionsCount;
        this.minQuestionsCountInGame = minQuestionsCountInGame;
        this.questionsCountToAdd = questionsCountToAdd;
        this.startCountTasks = startCountTasks;
    }

    public String getGameName() {
        return gameName;
    }

    public int getStartCountTasks() {
        return startCountTasks;
    }

    public void setStartCountTasks(int startCountTasks) {
        this.startCountTasks = startCountTasks;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int[] getGroupsIds() {
        return groupsIds;
    }

    public void setGroupsIds(int[] groupsIds) {
        this.groupsIds = groupsIds;
    }

    public int getMaxTimeMinutes() {
        return maxTimeMinutes;
    }

    public void setMaxTimeMinutes(int maxTimeMinutes) {
        this.maxTimeMinutes = maxTimeMinutes;
    }

    public int getMaxQuestionsCount() {
        return maxQuestionsCount;
    }

    public void setMaxQuestionsCount(int maxQuestionsCount) {
        this.maxQuestionsCount = maxQuestionsCount;
    }

    public int getMaxPerformedQuestionsCount() {
        return maxPerformedQuestionsCount;
    }

    public void setMaxPerformedQuestionsCount(int maxPerformedQuestionsCount) {
        this.maxPerformedQuestionsCount = maxPerformedQuestionsCount;
    }

    public int getMinQuestionsCountInGame() {
        return minQuestionsCountInGame;
    }

    public void setMinQuestionsCountInGame(int minQuestionsCountInGame) {
        this.minQuestionsCountInGame = minQuestionsCountInGame;
    }

    public int getQuestionsCountToAdd() {
        return questionsCountToAdd;
    }

    public void setQuestionsCountToAdd(int questionsCountToAdd) {
        this.questionsCountToAdd = questionsCountToAdd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return gameName.equals(game.gameName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameName);
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameName='" + gameName + '\'' +
                ", groupsIds=" + Arrays.toString(groupsIds) +
                ", maxTimeMinutes=" + maxTimeMinutes +
                ", maxQuestionsCount=" + maxQuestionsCount +
                ", maxPerformedQuestionsCount=" + maxPerformedQuestionsCount +
                ", minQuestionsCountInGame=" + minQuestionsCountInGame +
                ", questionsCountToAdd=" + questionsCountToAdd +
                ", startCountTasks=" + startCountTasks +
                '}';
    }
}

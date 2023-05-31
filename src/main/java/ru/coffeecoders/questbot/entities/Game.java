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
    private Integer maxTimeMinutes;

    @Column(name = "max_questions_count")
    private Integer maxQuestionsCount;

    @Column(name = "max_performed_questions_count")
    private Integer maxPerformedQuestionsCount;

    @Column(name = "min_questions_count_in_game")
    private Integer minQuestionsCountInGame;

    @Column(name = "questions_count_to_add")
    private Integer questionsCountToAdd;

    @Column(name = "start_count_tasks")
    private Integer startCountTasks;

    @Column(name = "addition_with_task")
    private boolean additionWithTask;

    @Column(name = "shuffle_questions")
    private boolean shuffleQuestions;

    public Game() {
    }

    public Game(NewGameCreatingState state) {
        this(
                state.getGameName(),
                state.getGroupsIds(),
                state.getMaxTimeMinutes(),
                state.getMaxQuestionsCount(),
                state.getMaxPerformedQuestionsCount(),
                state.getMinQuestionsCountInGame(),
                state.getQuestionsCountToAdd(),
                state.getStartCountTasks(),
                state.isAdditionWithTask(),
                state.isShuffleQuestions()
        );
    }

    public Game(String gameName, int[] groupsIds, Integer maxTimeMinutes, Integer maxQuestionsCount,
                Integer maxPerformedQuestionsCount, Integer minQuestionsCountInGame, Integer questionsCountToAdd,
                Integer startCountTasks, boolean additionWithTask, boolean shuffleQuestions)
    {
        this.gameName = gameName;
        this.groupsIds = groupsIds;
        this.maxTimeMinutes = maxTimeMinutes;
        this.maxQuestionsCount = maxQuestionsCount;
        this.maxPerformedQuestionsCount = maxPerformedQuestionsCount;
        this.minQuestionsCountInGame = minQuestionsCountInGame;
        this.questionsCountToAdd = questionsCountToAdd;
        this.startCountTasks = startCountTasks;
        this.additionWithTask = additionWithTask;
        this.shuffleQuestions = shuffleQuestions;
    }

    public boolean isShuffleQuestions() {
        return shuffleQuestions;
    }

    public void setShuffleQuestions(boolean shuffleQuestions) {
        this.shuffleQuestions = shuffleQuestions;
    }

    public boolean isAdditionWithTask() {
        return additionWithTask;
    }

    public void setAdditionWithTask(boolean additionWithTask) {
        this.additionWithTask = additionWithTask;
    }

    public String getGameName() {
        return gameName;
    }

    public Integer getStartCountTasks() {
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

    public Integer getMaxTimeMinutes() {
        return maxTimeMinutes;
    }

    public void setMaxTimeMinutes(int maxTimeMinutes) {
        this.maxTimeMinutes = maxTimeMinutes;
    }

    public Integer getMaxQuestionsCount() {
        return maxQuestionsCount;
    }

    public void setMaxQuestionsCount(int maxQuestionsCount) {
        this.maxQuestionsCount = maxQuestionsCount;
    }

    public Integer getMaxPerformedQuestionsCount() {
        return maxPerformedQuestionsCount;
    }

    public void setMaxPerformedQuestionsCount(int maxPerformedQuestionsCount) {
        this.maxPerformedQuestionsCount = maxPerformedQuestionsCount;
    }

    public Integer getMinQuestionsCountInGame() {
        return minQuestionsCountInGame;
    }

    public void setMinQuestionsCountInGame(int minQuestionsCountInGame) {
        this.minQuestionsCountInGame = minQuestionsCountInGame;
    }

    public Integer getQuestionsCountToAdd() {
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
                ", additionWithTask=" + additionWithTask +
                ", shuffleQuestions=" + shuffleQuestions +
                '}';
    }
}

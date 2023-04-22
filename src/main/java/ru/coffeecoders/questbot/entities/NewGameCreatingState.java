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
@Table(name = "new_game_creating_state")
public class NewGameCreatingState {

    @Id
    @Column(name = "initiator_chat_id")
    private long initiatorChatId;

    @Column(name = "game_name")
    private String gameName;

    @Column(name = "groups_ids")
    private int[] groupsIds;

    @Column(name = "start_count_tasks")
    private Integer startCountTasks;

    @Column(name = "max_questions_count")
    private Integer maxQuestionsCount;

    @Column(name = "max_performed_questions_count")
    private Integer maxPerformedQuestionsCount;

    @Column(name = "min_questions_count_in_game")
    private Integer minQuestionsCountInGame;

    @Column(name = "questions_count_to_add")
    private Integer questionsCountToAdd;

    @Column(name = "max_time_minutes")
    private Integer maxTimeMinutes;

    public long getInitiatorChatId() {
        return initiatorChatId;
    }

    public void setInitiatorChatId(long initiatorChatId) {
        this.initiatorChatId = initiatorChatId;
    }

    public String getGameName() {
        return gameName;
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

    public Integer getStartCountTasks() {
        return startCountTasks;
    }

    public void setStartCountTasks(Integer startCountTasks) {
        this.startCountTasks = startCountTasks;
    }

    public Integer getMaxQuestionsCount() {
        return maxQuestionsCount;
    }

    public void setMaxQuestionsCount(Integer maxQuestionsCount) {
        this.maxQuestionsCount = maxQuestionsCount;
    }

    public Integer getMaxPerformedQuestionsCount() {
        return maxPerformedQuestionsCount;
    }

    public void setMaxPerformedQuestionsCount(Integer maxPerformedQuestionsCount) {
        this.maxPerformedQuestionsCount = maxPerformedQuestionsCount;
    }

    public Integer getMinQuestionsCountInGame() {
        return minQuestionsCountInGame;
    }

    public void setMinQuestionsCountInGame(Integer minQuestionsCountInGame) {
        this.minQuestionsCountInGame = minQuestionsCountInGame;
    }

    public Integer getQuestionsCountToAdd() {
        return questionsCountToAdd;
    }

    public void setQuestionsCountToAdd(Integer questionsCountToAdd) {
        this.questionsCountToAdd = questionsCountToAdd;
    }

    public Integer getMaxTimeMinutes() {
        return maxTimeMinutes;
    }

    public void setMaxTimeMinutes(Integer maxTimeMinutes) {
        this.maxTimeMinutes = maxTimeMinutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewGameCreatingState that = (NewGameCreatingState) o;
        return initiatorChatId == that.initiatorChatId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(initiatorChatId);
    }

    @Override
    public String toString() {
        return "NewGameCreatingState{" +
                "initiatorChatId=" + initiatorChatId +
                ", gameName='" + gameName + '\'' +
                ", groupsIds=" + Arrays.toString(groupsIds) +
                ", startCountTasks=" + startCountTasks +
                ", maxQuestionsCount=" + maxQuestionsCount +
                ", maxPerformedQuestionsCount=" + maxPerformedQuestionsCount +
                ", minQuestionsCountInGame=" + minQuestionsCountInGame +
                ", questionsCountToAdd=" + questionsCountToAdd +
                ", maxTimeMinutes=" + maxTimeMinutes +
                '}';
    }
}

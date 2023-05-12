package ru.coffeecoders.questbot.entities;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * @author ezuykow
 */
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @Column(name = "task_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int taskId;

    @Column(name = "game_name")
    private String gameName;

    @Column(name = "question_id")
    private int questionId;

    @Column(name = "performed_team_name")
    private String performedTeamName;

    @Column(name = "chat_id")
    private long chatId;

    @Column(name = "actual")
    private boolean actual;

    @Column(name = "task_number")
    private int taskNumber;

    public Task() {
    }

    public Task(String gameName, int questionId, String performedTeamName, long chatId, int taskNumber) {
        this.gameName = gameName;
        this.questionId = questionId;
        this.performedTeamName = performedTeamName;
        this.chatId = chatId;
        this.taskNumber = taskNumber;
        this.actual = false;
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    public boolean isActual() {
        return actual;
    }

    public void setActual(boolean actual) {
        this.actual = actual;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getPerformedTeamName() {
        return performedTeamName;
    }

    public void setPerformedTeamName(String performedTeamName) {
        this.performedTeamName = performedTeamName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId);
    }


    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", gameName='" + gameName + '\'' +
                ", questionId=" + questionId +
                ", performedTeamName='" + performedTeamName + '\'' +
                ", chatId=" + chatId +
                ", actual=" + actual +
                ", taskNumber=" + taskNumber +
                '}';
    }
}

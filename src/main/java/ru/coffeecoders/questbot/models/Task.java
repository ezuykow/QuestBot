package ru.coffeecoders.questbot.models;

import java.util.Objects;

public class Task {

    private int taskId;
    private long questionId;
    private String performedTeamName;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
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
                ", questionId=" + questionId +
                ", performedTeamName='" + performedTeamName + '\'' +
                '}';
    }
}

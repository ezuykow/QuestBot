package ru.coffeecoders.questbot.entities;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * @author ezuykow
 */
@Entity
@Table(name = "question_groups")
public class QuestionGroup {

    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long groupId;

    @Column(name = "group_name")
    private String groupName;

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionGroup that = (QuestionGroup) o;
        return groupId == that.groupId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId);
    }

    @Override
    public String toString() {
        return "QuestionGroup{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}

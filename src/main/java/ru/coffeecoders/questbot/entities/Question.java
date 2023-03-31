package ru.coffeecoders.questbot.entities;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

/**
 * @author ezuykow
 */
@Entity
@Table(name = "questions")
public class Question {

    @Id
    @Column(name = "question_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int questionId;

    @Column(name = "question")
    private String question;

    @Column(name = "answer_format")
    private String answerFormat;

    @Column(name = "answer")
    private String answer;

    @Column(name = "map_url")
    private String mapUrl;

    @Column(name = "last_usage")
    private Date lastUsage;

    @Column(name = "group")
    private String group;

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswerFormat() {
        return answerFormat;
    }

    public void setAnswerFormat(String answerFormat) {
        this.answerFormat = answerFormat;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public Date getLastUsage() {
        return lastUsage;
    }

    public void setLastUsage(Date lastUsage) {
        this.lastUsage = lastUsage;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return questionId == question.questionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId);
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionId=" + questionId +
                ", question='" + question + '\'' +
                ", answerFormat='" + answerFormat + '\'' +
                ", answer='" + answer + '\'' +
                ", mapUrl present='" + !mapUrl.isBlank() + '\'' +
                ", lastUsage=" + lastUsage +
                ", group='" + group + '\'' +
                '}';
    }
}

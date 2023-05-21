package ru.coffeecoders.questbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.coffeecoders.questbot.entities.Question;

import java.util.List;
import java.util.Optional;

/**
 * @author ezuykow
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByGroup(String group);

    @Query(value = "SELECT question FROM questions", nativeQuery = true)
    List<String> getQuestionsTexts();

    Optional<Question> findQuestionByQuestion(String question);
}

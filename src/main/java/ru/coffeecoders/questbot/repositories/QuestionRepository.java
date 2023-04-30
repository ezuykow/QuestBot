package ru.coffeecoders.questbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.coffeecoders.questbot.entities.Question;

import java.util.List;

/**
 * @author ezuykow
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByGroup(String group);
}

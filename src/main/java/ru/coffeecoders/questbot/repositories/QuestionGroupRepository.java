package ru.coffeecoders.questbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.coffeecoders.questbot.entities.QuestionGroup;

import java.util.Optional;

/**
 * @author ezuykow
 */
@Repository
public interface QuestionGroupRepository extends JpaRepository<QuestionGroup, Long> {

    /**
     * @author ezuykow
     */
    Optional<QuestionGroup> findQuestionGroupByGroupName(String groupName);
}

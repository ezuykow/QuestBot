package ru.coffeecoders.questbot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.entities.QuestionGroup;
import ru.coffeecoders.questbot.repositories.QuestionGroupRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class QuestionGroupService {

    Logger logger = LoggerFactory.getLogger(QuestionGroupService.class);
    private final QuestionGroupRepository questionGroupRepository;

    public QuestionGroupService(QuestionGroupRepository questionGroupRepository) {
        this.questionGroupRepository = questionGroupRepository;
    }

    public List<QuestionGroup> findAll() {
        List<QuestionGroup> list = questionGroupRepository.findAll();
        if (!list.isEmpty()) {
            logger.info("QuestionGroups are displaying");
        } else {
            logger.warn("No questionGroups found");
        }
        return list;
    }

    //TODO Integer или int?
    public Optional<QuestionGroup> findById(int id) {
        Optional<QuestionGroup> optional = questionGroupRepository.findByGroupId(id);
        return Optional.ofNullable(optional
                .orElseThrow(() -> {
                    logger.warn("QuestionGroup not found with groupId = {}", id);
                    throw new NoSuchElementException("QuestionGroup not found with groupId" + id);
                }));
    }
}

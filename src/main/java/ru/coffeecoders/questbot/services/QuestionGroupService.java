package ru.coffeecoders.questbot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.entities.GlobalChat;
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
        logger.info("QuestionGroups {} displaying", list.isEmpty() ? "are not" : "are");
        return list;
    }

    public Optional<QuestionGroup> findById(int id) {
        Optional<QuestionGroup> optional = questionGroupRepository.findById(id);
        logger.info("Player {} with id = {}", optional.isPresent() ? "found" : "not found", id);
        return optional;
    }

    public QuestionGroup save(QuestionGroup questionGroup) {
        logger.info("QuestionGroup = {} has been saved", questionGroup);
        return questionGroupRepository.save(questionGroup);
    }
}
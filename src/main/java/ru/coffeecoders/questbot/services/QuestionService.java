package ru.coffeecoders.questbot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.repositories.QuestionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    Logger logger = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository repository;

    public QuestionService(QuestionRepository repository) {
        this.repository = repository;
    }

    public List<Question> findAll() {
        List<Question> list = repository.findAll();
        logger.info("Questions {} displaying", list.isEmpty() ? "are not" : "are");
        return list;
    }

    public Optional<Question> findById(long id) {
        Optional<Question> optional = repository.findById(id);
        logger.info("Question with id = {} {}", id, optional.isPresent() ? "found" : "not found");
        return optional;
    }

    public List<Question> findByGroup(String group) {
        List<Question> list = repository.findByGroup(group);
        logger.info("Questions with group name = {} {} displaying", group, list.isEmpty() ? "are not" : "are");
        return list;
    }

    public Question save(Question question) {
        logger.info("Question = {} has been saved", question);
        return repository.save(question);
    }

    public List<Question> saveAll(List<Question> questionList) {
        logger.info("Questions = {} has been saved", questionList);
        return repository.saveAll(questionList);
    }
}
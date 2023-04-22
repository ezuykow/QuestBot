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
    private final QuestionRepository questionRepository;
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> findAll() {
        List<Question> list = questionRepository.findAll();
        logger.info("Questions {} displaying", list.isEmpty() ? "are not" : "are");
        return list;
    }

    public Optional<Question> findById(long id) {
        Optional<Question> question = questionRepository.findById(id);
        logger.info("Question {} with id = {}", question.isPresent() ? "found" : "not found", id);
        return question;
    }

    /**
     * @author anatoliy
     * @Redact: ezuykow
     */
    public List<Question> findByGroupName(String groupName) {
        List<Question> list = questionRepository.findByGroup(groupName);
        logger.info("Questions {} with group = {} displaying", list.isEmpty() ? "are not" : "are",groupName);
        return list;
    }

    public Question save(Question question) {
        logger.info("Question = {} has been saved", question);
        return questionRepository.save(question);
    }

    public List<Question> saveAll(List<Question> questionList) {
        logger.info("Questions = {} has been saved", questionList);
        return questionRepository.saveAll(questionList);
    }
}
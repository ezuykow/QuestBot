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
        if (!list.isEmpty()) {
            logger.info("Questions are displaying");
        } else {
            logger.warn("No questions found");
        }
        return list;
    }

    public Optional<Question> findById(int id) {
        Optional<Question> question = questionRepository.findByQuestionId(id);
        if (question.isPresent()) {
            logger.info("Question found with id = {}", id);
        } else {
            logger.warn("Question not found with id = {}", id);
        }
        return question;
    }

    public List<Question> findByGroup(String group) {
        List<Question> list = questionRepository.findByGroup(group);
        if (!list.isEmpty()) {
            logger.info("Questions are displaying");
        } else {
            logger.warn("No question found");
        }
        return list;
    }

    public Question save(Question question) {
        logger.info("Question = {} has been saved", question);
        return questionRepository.save(question);
    }
}

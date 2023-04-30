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
        logger.info("Questions are {} displaying", list.isEmpty() ? "not" : "");
        return list;
    }

    public Optional<Question> findById(long id) {
        Optional<Question> optional = repository.findById(id);
        logger.info("Question with id = {} {} found", id, optional.isPresent() ? "" : "not");
        return optional;
    }

    /**
     * @author anatoliy
     * @Redact: ezuykow
     */
    public List<Question> findByGroupName(String groupName) {
        List<Question> list = repository.findByGroup(groupName);
        logger.info("Questions with group name = {} are {} displaying",groupName, list.isEmpty() ? "not" : "");
        return list;
    }

    public Question save(Question question) {
        logger.info("Question = {} has been saved", question);
        return repository.save(question);
    }

    public void saveAll(List<Question> questionList) {
        logger.info("Questions = {} has been saved", questionList);
        repository.saveAll(questionList);
    }

    /**
     * Удаляет вопрос из БД
     * @param question удаляемый вопрос
     * @author ezuykow
     */
    public void delete(Question question) {
        logger.info("Question = {} has been deleted", question);
        repository.delete(question);
    }
}
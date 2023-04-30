package ru.coffeecoders.questbot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.entities.QuestionGroup;
import ru.coffeecoders.questbot.exceptions.NonExistentQuestionGroup;
import ru.coffeecoders.questbot.repositories.QuestionGroupRepository;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionGroupService {

    Logger logger = LoggerFactory.getLogger(QuestionGroupService.class);

    private final QuestionGroupRepository repository;
    private final QuestionService questionService;
    private final GameService gameService;

    public QuestionGroupService(QuestionGroupRepository repository, QuestionService questionService,
                                GameService gameService) {
        this.repository = repository;
        this.questionService = questionService;
        this.gameService = gameService;
    }

    public List<QuestionGroup> findAll() {
        List<QuestionGroup> list = repository.findAll();
        logger.info("QuestionGroups are {} displaying", list.isEmpty() ? "not" : "");
        return list;
    }

    public Optional<QuestionGroup> findById(long id) {
        Optional<QuestionGroup> optional = repository.findById(id);
        logger.info("QuestionGroup with id = {} {} found", id, optional.isPresent() ? "" : "not");
        return optional;
    }

    public QuestionGroup save(QuestionGroup questionGroup) {
        logger.info("QuestionGroup = {} has been saved", questionGroup);
        return repository.save(questionGroup);
    }

    /**
     * Находит в БД {@link QuestionGroup} по названию
     * @param groupName название группы
     * @return {@link Optional} с найденной группой, либо пустой, если такой группы не было в БД
     * @author ezuykow
     */
    public Optional<QuestionGroup> findByGroupName(String groupName) {
        Optional<QuestionGroup> optional = repository.findQuestionGroupByGroupName(groupName);
        logger.info("QuestionGroup with name = {} {} found", groupName, optional.isPresent() ? "" : "not");
        return optional;
    }

    /**
     * Удаляет группу из БД
     * @param questionGroup удаляемая группа
     * @author ezuykow
     */
    public void delete(QuestionGroup questionGroup) {
        logger.info("QuestionGroup = {} has been deleted", questionGroup);
        repository.delete(questionGroup);
    }

    /**
     * Удаляет группу из БД, если вопросов с такой группой нет
     * @param groupName название группы
     * @author ezuykow
     */
    //TODO logger
    public void deleteQuestionGroupIfNoQuestionsWithIt(String groupName) {
        if (questionService.findByGroupName(groupName).isEmpty()) {
            findByGroupName(groupName).ifPresentOrElse(
                    qg -> {
                        delete(qg);
                        gameService.setGroupIdIfItsDeleted(qg.getGroupId());
                    },
                    NonExistentQuestionGroup::new
            );
        }
    }
}
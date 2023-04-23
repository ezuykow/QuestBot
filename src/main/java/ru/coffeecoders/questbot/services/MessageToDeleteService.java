package ru.coffeecoders.questbot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.entities.MessageToDelete;
import ru.coffeecoders.questbot.repositories.MessageToDeleteRepository;

import java.util.List;

/**
 * @author ezuykow
 */
@Service
public class MessageToDeleteService {

    Logger logger = LoggerFactory.getLogger(MessageToDeleteService.class);

    private final MessageToDeleteRepository repository;

    public MessageToDeleteService(MessageToDeleteRepository repository) {
        this.repository = repository;
    }

    /**
     * Сохраняет объект {@link MessageToDelete} в БД
     * @param messageToDelete сохраняемый объект {@link MessageToDelete}
     */
    public void save(MessageToDelete messageToDelete) {
        logger.info("MessageToDelete = {} has been saved", messageToDelete);
        repository.save(messageToDelete);
    }

    /**
     * Сохраняет объекты {@link MessageToDelete} в БД
     * @param mtds {@link List} сохраняемых объектов {@link MessageToDelete}
     */
    public void saveAll(List<MessageToDelete> mtds) {
        logger.info("List MessageToDelete = {} has been saved", mtds);
        repository.saveAll(mtds);
    }

    /**
     * Возвращает список всех {@link MessageToDelete} из БД
     * @return {@link List<MessageToDelete>} объектов {@link MessageToDelete}
     */
    public List<MessageToDelete> findAll() {
        List<MessageToDelete> list = repository.findAll();
        logger.info("List<MessageToDelete> are {} displaying", list.isEmpty() ? "not" : "");
        return list;
    }

    /**
     * Возвращает список всех {@link MessageToDelete} из БД с полем user_id = userId
     * @param userId искомый userId
     * @return {@link List<MessageToDelete>} объектов {@link MessageToDelete}
     */
    public List<MessageToDelete> findByUserId(long userId) {
        List<MessageToDelete> list = repository.findByUserId(userId);
        logger.info("MessageToDelete with user_id = {} {} found", userId, list.isEmpty() ? "not" : "");
        return list;
    }

    /**
     * Удаляет объект {@link MessageToDelete} из БД
     * @param messageToDelete объект, который необходимо удалить
     */
    public void delete(MessageToDelete messageToDelete) {
        logger.info("MessageToDelete = {} has been deleted", messageToDelete);
        repository.delete(messageToDelete);
    }

    /**
     * Удаляет объекты {@link MessageToDelete} из БД
     * @param mtds {@link List} объектов, которые необходимо удалить
     */
    public void  deleteAll(List<MessageToDelete> mtds) {
        logger.info("List<MessageToDelete> = {} has been deleted", mtds);
        repository.deleteAll(mtds);
    }
}
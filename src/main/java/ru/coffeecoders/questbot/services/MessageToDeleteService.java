package ru.coffeecoders.questbot.services;

import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.entities.MessageToDelete;
import ru.coffeecoders.questbot.repositories.MessageToDeleteRepository;

import java.util.List;

/**
 * @author ezuykow
 */
@Service
public class MessageToDeleteService {

    private final MessageToDeleteRepository repository;

    public MessageToDeleteService(MessageToDeleteRepository repository) {
        this.repository = repository;
    }

    /**
     * Сохраняет объект {@link MessageToDelete} в БД
     * @param messageToDelete сохраняемый объект {@link MessageToDelete}
     */
    public void save(MessageToDelete messageToDelete) {
        repository.save(messageToDelete);
    }

    /**
     * Сохраняет объекты {@link MessageToDelete} в БД
     * @param mtds {@link List} сохраняемых объектов {@link MessageToDelete}
     */
    public void saveAll(List<MessageToDelete> mtds) {
        repository.saveAll(mtds);
    }

    /**
     * Возвращает список всех {@link MessageToDelete} из БД
     * @return {@link List<MessageToDelete>} объектов {@link MessageToDelete}
     */
    public List<MessageToDelete> findAll() {
        return repository.findAll();
    }

    /**
     * Возвращает список всех {@link MessageToDelete} из БД с полем user_id = userId
     * @param userId искомый userId
     * @return {@link List<MessageToDelete>} объектов {@link MessageToDelete}
     */
    public List<MessageToDelete> findByUserId(long userId) {
        return repository.findByUserId(userId);
    }

    /**
     * Удаляет объект {@link MessageToDelete} из БД
     * @param messageToDelete объект, который необходимо удалить
     */
    public void delete(MessageToDelete messageToDelete) {
        repository.delete(messageToDelete);
    }

    /**
     * Удаляет объекты {@link MessageToDelete} из БД
     * @param mtds {@link List} объектов, которые необходимо удалить
     */
    public void  deleteAll(List<MessageToDelete> mtds) {
        repository.deleteAll(mtds);
    }
}

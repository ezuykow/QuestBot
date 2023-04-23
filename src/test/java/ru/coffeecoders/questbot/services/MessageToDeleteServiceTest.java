package ru.coffeecoders.questbot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.MessageToDelete;
import ru.coffeecoders.questbot.repositories.MessageToDeleteRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

/**
 * @author Anatoliy Shikin
 */
@ExtendWith(MockitoExtension.class)
class MessageToDeleteServiceTest {

    @Mock
    private MessageToDeleteRepository repository;

    @InjectMocks
    private MessageToDeleteService service;

    private MessageToDelete message;
    private long id;

    @BeforeEach
    void setUp() {
        message = new MessageToDelete();
        id = 111L;
    }

    @Test
    void save() {
        service.save(message);
        Mockito.verify(repository).save(message);
    }

    @Test
    void saveAll() {
        service.saveAll(anyList());
        Mockito.verify(repository).saveAll(anyList());
    }

    @Test
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(message, new MessageToDelete(), new MessageToDelete()));
        assertEquals(3, service.findAll().size());
        assertTrue(service.findAll().contains(message));
        Mockito.verify(repository, times(2)).findAll();
    }

    @Test
    void findAllEmptyListTest() {
        when(repository.findAll()).thenReturn(List.of());
        assertTrue(service.findAll().isEmpty());
        Mockito.verify(repository).findAll();
    }

    @Test
    void findByUserId() {
        when(repository.findByUserId(anyLong())).thenReturn(List.of(message));
        assertTrue(service.findByUserId(id).contains(message));
        assertEquals(List.of(message), service.findByUserId(id));
        Mockito.verify(repository, times(2)).findByUserId(id);
    }

    @Test
    void findByGameNameEmpty() {
        when(repository.findByUserId(id)).thenReturn(List.of());
        assertTrue(service.findByUserId(id).isEmpty());
        Mockito.verify(repository).findByUserId(id);
    }

    @Test
    void delete() {
        service.delete(message);
        Mockito.verify(repository).delete(message);
    }

    @Test
    void deleteAll() {
        service.deleteAll(anyList());
        Mockito.verify(repository).deleteAll(anyList());
    }
}
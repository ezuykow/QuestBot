package ru.coffeecoders.questbot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.Task;
import ru.coffeecoders.questbot.repositories.TaskRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService service;

    private Task task;
    private int id;
    private String name;

    @BeforeEach
    void setUp() {
        task = new Task();
        id = 111;
        name = "Test";
    }

    @Test
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(task, new Task(), new Task()));
        assertTrue(service.findAll().contains(task));
        assertEquals(3, service.findAll().size());
        verify(repository, times(2)).findAll();
    }

    @Test
    void findAllEmptyList() {
        when(repository.findAll()).thenReturn(List.of());
        assertTrue(service.findAll().isEmpty());
        verify(repository).findAll();
    }
    @Test
    void deleteById() {
        doNothing().when(repository).deleteById(anyLong());
        service.deleteById(id);
        verify(repository, times(1)).deleteById((long) id);
    }

    @Test
    void save() {
        when(repository.save(any(Task.class))).thenReturn(task);
        assertEquals(task, service.save(task));
        verify(repository).save(task);
    }

    @Test
    void findByGameName() {
        when(repository.findByGameName(anyString())).thenReturn(List.of(task));
        assertTrue(service.findByGameName(name).contains(task));
        assertEquals(List.of(task), service.findByGameName(name));
        verify(repository, times(2)).findByGameName(name);
    }

    @Test
    void findByGameNameEmpty() {
        when(repository.findByGameName(name)).thenReturn(List.of());
        assertTrue(service.findByGameName(name).isEmpty());
        verify(repository).findByGameName(name);
    }

    @Test
    void saveAll() {
        service.saveAll(anyList());
        verify(repository).saveAll(anyList());
    }

    @Test
    void deleteAllByChatId() {
        service.deleteAllByChatId(id);
        verify(repository).deleteAllByChatId(id);
    }
}
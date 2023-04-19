package ru.coffeecoders.questbot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.Task;
import ru.coffeecoders.questbot.repositories.TaskRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService service;

    private Task task;
    private long id;
    private String name;

    @BeforeEach
    void setUp() {
        task = new Task();
        id = 111L;
        name = "Test";
    }

    @Test
    void findAll() {
        task.setTaskId(id);
        when(repository.findAll()).thenReturn(List.of(task, new Task(), new Task()));
        assertTrue(service.findAll().contains(task));
        assertEquals(3, service.findAll().size());
        Mockito.verify(repository, times(2)).findAll();
    }

    @Test
    void findAllEmptyList() {
        when(repository.findAll()).thenReturn(List.of());
        assertTrue(service.findAll().isEmpty());
        Mockito.verify(repository).findAll();
    }
    //TODO
    @Test
    void deleteById() {
    }

    @Test
    void save() {
        when(repository.save(any(Task.class))).thenReturn(task);
        assertEquals(task, service.save(task));
        Mockito.verify(repository).save(task);
    }

    @Test
    void findByGameName() {
        when(repository.findByGameName(anyString())).thenReturn(List.of(task));
        assertTrue(service.findByGameName(name).contains(task));
        assertEquals(List.of(task), service.findByGameName(name));
        Mockito.verify(repository, times(2)).findByGameName(name);
    }

    @Test
    void findByGameNameEmpty() {
        when(repository.findByGameName(name)).thenReturn(List.of());
        assertTrue(service.findByGameName(name).isEmpty());
        Mockito.verify(repository).findByGameName(name);
    }
}
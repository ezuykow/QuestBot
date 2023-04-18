package ru.coffeecoders.questbot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.entities.Task;
import ru.coffeecoders.questbot.repositories.TaskRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

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
        when(taskRepository.findAll()).thenReturn(List.of(task, new Task(), new Task()));
        assertTrue(taskService.findAll().contains(task));
        assertEquals(3, taskService.findAll().size());
        Mockito.verify(taskRepository, times(2)).findAll();
    }

    @Test
    void findAllEmptyList() {
        when(taskRepository.findAll()).thenReturn(List.of());
        assertTrue(taskService.findAll().isEmpty());
        Mockito.verify(taskRepository).findAll();
    }

    @Test
    void deleteById() {
    }

    @Test
    void save() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        assertEquals(task, taskService.save(task));
        Mockito.verify(taskRepository).save(task);
    }

    @Test
    void findByGameName() {
        when(taskRepository.findByGameName(any(String.class))).thenReturn(List.of(task));
        assertTrue(taskService.findByGameName(name).contains(task));
        assertEquals(List.of(task), taskService.findByGameName(name));
        Mockito.verify(taskRepository, times(2)).findByGameName(name);
    }
}
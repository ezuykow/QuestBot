package ru.coffeecoders.questbot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.repositories.QuestionRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionRepository repository;

    @InjectMocks
    private QuestionService service;

    private Question question;
    private int id;
    private String name;

    @BeforeEach
    void setUp() {
        question = new Question();
        name = "Test";
        id = 111;
    }

    @Test
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(question, new Question(), new Question()));
        assertTrue(service.findAll().contains(question));
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
    void findById() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(question));
        assertTrue(service.findById(id).isPresent());
        assertEquals(question, service.findById(id).get());
        verify(repository, times(2)).findById((long) id);
    }

    @Test
    void findByIdEmpty() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertTrue(service.findById(id).isEmpty());
        verify(repository).findById((long) id);
    }

    @Test
    void findByGroup() {
        when(repository.findByGroup(anyString())).thenReturn(List.of(question));
        assertTrue(service.findByGroupName(name).contains(question));
        assertEquals(List.of(question), service.findByGroupName(name));
        verify(repository, times(2)).findByGroup(name);
    }

    @Test
    void findByGroupEmpty() {
        when(repository.findByGroup(anyString())).thenReturn(List.of());
        assertTrue(service.findByGroupName(name).isEmpty());
        verify(repository).findByGroup(name);
    }

    @Test
    void save() {
        when(repository.save(any(Question.class))).thenReturn(question);
        assertEquals(question, service.save(question));
        verify(repository).save(question);
    }

    @Test
    void saveAll() {
        service.saveAll(List.of(question, new Question(), new Question()));
        verify(repository).saveAll(anyList());
    }

    @Test
    void delete() {
        service.delete(question);
        verify(repository).delete(question);
    }
}
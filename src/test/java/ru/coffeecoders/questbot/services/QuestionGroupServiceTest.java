package ru.coffeecoders.questbot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.QuestionGroup;
import ru.coffeecoders.questbot.repositories.QuestionGroupRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionGroupServiceTest {

    @Mock
    private QuestionGroupRepository repository;

    @InjectMocks
    private QuestionGroupService service;

    private int id;
    private QuestionGroup questionGroup;
    private String groupName;

    @BeforeEach
    void setUp() {
        questionGroup = new QuestionGroup();
        id = 111;
        groupName = "Хардовые";
    }

    @Test
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(questionGroup, new QuestionGroup(), new QuestionGroup()));
        assertTrue(service.findAll().contains(questionGroup));
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
        when(repository.findById(anyLong())).thenReturn(Optional.of(questionGroup));
        assertTrue(service.findById(id).isPresent());
        assertEquals(questionGroup, service.findById(id).get());
        verify(repository, times(2)).findById((long) id);
    }

    @Test
    void findByIdEmpty() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertTrue(service.findById(id).isEmpty());
        verify(repository).findById((long) id);
    }

    @Test
    void save() {
        when(repository.save(any(QuestionGroup.class))).thenReturn(questionGroup);
        assertEquals(questionGroup, service.save(questionGroup));
        verify(repository).save(questionGroup);
    }

    @Test
    void findByGroupName() {
        when(repository.findQuestionGroupByGroupName(anyString())).thenReturn(Optional.of(questionGroup));
        assertTrue(service.findByGroupName(groupName).isPresent());
        assertEquals(questionGroup, service.findByGroupName(groupName).get());
        verify(repository, times(2)).findQuestionGroupByGroupName(groupName);
    }

    @Test
    void findByGroupNameEmpty() {
        when(repository.findQuestionGroupByGroupName(anyString())).thenReturn(Optional.empty());
        assertTrue(service.findByGroupName(groupName).isEmpty());
        verify(repository).findQuestionGroupByGroupName(groupName);
    }

    @Test
    void delete() {
        service.delete(questionGroup);
        verify(repository).delete(questionGroup);
    }

    //TODO
    @Test
    void deleteQuestionGroupIfNoQuestionsWithIt() {

    }
}
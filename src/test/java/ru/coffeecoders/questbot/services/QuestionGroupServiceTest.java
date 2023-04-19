package ru.coffeecoders.questbot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.QuestionGroup;
import ru.coffeecoders.questbot.repositories.QuestionGroupRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionGroupServiceTest {

    @Mock
    private QuestionGroupRepository repository;

    @InjectMocks
    private QuestionGroupService service;

    private long id;
    private QuestionGroup questionGroup;

    @BeforeEach
    void setUp() {
        questionGroup = new QuestionGroup();
        id = 111L;
    }

    @Test
    void findAll() {
        questionGroup.setGroupId(id);
        when(repository.findAll()).thenReturn(List.of(questionGroup, new QuestionGroup(), new QuestionGroup()));
        assertTrue(service.findAll().contains(questionGroup));
        assertEquals(3, service.findAll().size());
        Mockito.verify(repository, times(2)).findAll();
    }

    @Test
    void findAllEmptyList() {
        when(repository.findAll()).thenReturn(List.of());
        assertTrue(service.findAll().isEmpty());
        Mockito.verify(repository).findAll();
    }

    @Test
    void findById() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(questionGroup));
        assertTrue(service.findById(id).isPresent());
        assertEquals(questionGroup, service.findById(id).get());
        Mockito.verify(repository, times(2)).findById(id);
    }

    @Test
    void findByIdEmpty() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertTrue(service.findById(id).isEmpty());
        Mockito.verify(repository).findById(id);
    }

    @Test
    void save() {
        when(repository.save(any(QuestionGroup.class))).thenReturn(questionGroup);
        assertEquals(questionGroup, service.save(questionGroup));
        Mockito.verify(repository).save(questionGroup);
    }
}
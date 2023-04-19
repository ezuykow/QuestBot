package ru.coffeecoders.questbot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.Player;
import ru.coffeecoders.questbot.entities.QuestionGroup;
import ru.coffeecoders.questbot.repositories.QuestionGroupRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class QuestionGroupServiceTest {

    @Mock
    private QuestionGroupRepository questionGroupRepository;

    @InjectMocks
    private QuestionGroupService questionGroupService;

    private int id;
    private QuestionGroup questionGroup;

    @BeforeEach
    void setUp() {
        questionGroup = new QuestionGroup();
        id = 111;
    }

    @Test
    void findAll() {
        questionGroup.setGroupId(id);
        when(questionGroupRepository.findAll()).thenReturn(List.of(questionGroup, new QuestionGroup(), new QuestionGroup()));
        assertTrue(questionGroupService.findAll().contains(questionGroup));
        assertEquals(3, questionGroupService.findAll().size());
        Mockito.verify(questionGroupRepository, times(2)).findAll();
    }

    @Test
    void findAllEmptyList() {
        when(questionGroupRepository.findAll()).thenReturn(List.of());
        assertTrue(questionGroupService.findAll().isEmpty());
        Mockito.verify(questionGroupRepository).findAll();
    }

    @Test
    void findById() {
        when(questionGroupRepository.findById(any(Long.class))).thenReturn(Optional.of(questionGroup));
        assertTrue(questionGroupService.findById(id).isPresent());
        assertEquals(questionGroup, questionGroupService.findById(id).get());
        Mockito.verify(questionGroupRepository, times(2)).findById((long) id);
    }

    @Test
    void findByIdEmpty() {
        when(questionGroupRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertTrue(questionGroupService.findById(id).isEmpty());
        Mockito.verify(questionGroupRepository).findById((long) id);
    }

    @Test
    void save() {
        when(questionGroupRepository.save(any(QuestionGroup.class))).thenReturn(questionGroup);
        assertEquals(questionGroup, questionGroupService.save(questionGroup));
        Mockito.verify(questionGroupRepository).save(questionGroup);
    }
}
package ru.coffeecoders.questbot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.entities.QuestionGroup;
import ru.coffeecoders.questbot.repositories.QuestionRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {
    @Mock
    private QuestionRepository questionRepository;
    @InjectMocks
    private QuestionService questionService;
    private Question question;
    private long id;
    private String name;
    @BeforeEach
    void setUp() {
        question = new Question();
        name = "Test";
        id = 111L;
    }

    @Test
    void findAll() {
        question.setQuestionId(id);
        when(questionRepository.findAll()).thenReturn(List.of(question, new Question(), new Question()));
        assertTrue(questionService.findAll().contains(question));
        assertEquals(3, questionService.findAll().size());
        Mockito.verify(questionRepository, times(2)).findAll();
    }

    @Test
    void findAllEmptyList() {
        when(questionRepository.findAll()).thenReturn(List.of());
        assertTrue(questionService.findAll().isEmpty());
        Mockito.verify(questionRepository).findAll();
    }

    @Test
    void findById() {
        when(questionRepository.findById(any(Long.class))).thenReturn(Optional.of(question));
        assertTrue(questionService.findById(id).isPresent());
        assertEquals(question, questionService.findById(id).get());
        Mockito.verify(questionRepository, times(2)).findById(id);
    }

    @Test
    void findByIdEmpty() {
        when(questionRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertTrue(questionService.findById(id).isEmpty());
        Mockito.verify(questionRepository).findById(id);
    }

    @Test
    void findByGroup() {
        when(questionRepository.findByGroup(any(String.class))).thenReturn(List.of(question));
        assertTrue(questionService.findByGroup(name).contains(question));
//        assertEquals(List.of(question), questionService.findByGroup(name));
        Mockito.verify(questionRepository, times(1)).findByGroup(name);
    }

    @Test
    void findByGroupEmpty() {
        when(questionRepository.findByGroup(any(String.class))).thenReturn(List.of());
        assertTrue(questionService.findByGroup(name).isEmpty());
        Mockito.verify(questionRepository).findByGroup(name);
    }

    @Test
    void save() {
        when(questionRepository.save(any(Question.class))).thenReturn(question);
        assertEquals(question, questionService.save(question));
        Mockito.verify(questionRepository).save(question);
    }

    @Test
    void saveAll() {
    }
}
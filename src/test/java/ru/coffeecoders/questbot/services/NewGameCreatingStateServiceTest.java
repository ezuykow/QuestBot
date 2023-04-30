package ru.coffeecoders.questbot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.NewGameCreatingState;
import ru.coffeecoders.questbot.repositories.NewGameCreatingStateRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Anatoliy Shikin
 */
@ExtendWith(MockitoExtension.class)
class NewGameCreatingStateServiceTest {

    @Mock
    private NewGameCreatingStateRepository repository;

    @InjectMocks
    private NewGameCreatingStateService service;

    private NewGameCreatingState state;
    private long chatId;

    @BeforeEach
    void setUp() {
        state = new NewGameCreatingState();
        chatId = 111L;
    }

    @Test
    void save() {
        service.save(state);
        verify(repository).save(state);
    }

    @Test
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(state, new NewGameCreatingState(), new NewGameCreatingState()));
        assertTrue(service.findAll().contains(state));
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
        when(repository.findById(anyLong())).thenReturn(Optional.of(state));
        assertTrue(service.findById(chatId).isPresent());
        assertEquals(state, service.findById(chatId).get());
        verify(repository, times(2)).findById(chatId);
    }

    @Test
    void findByIdEmpty() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertTrue(service.findById(chatId).isEmpty());
        verify(repository).findById(chatId);
    }

    @Test
    void delete() {
        service.delete(state);
        verify(repository).delete(state);
    }
}
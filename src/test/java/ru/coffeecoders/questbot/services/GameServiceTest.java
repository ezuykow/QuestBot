package ru.coffeecoders.questbot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.repositories.GameRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock
    private GameRepository repository;

    @InjectMocks
    private GameService service;

    private String name;
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
        name = "testName";
    }

    @Test
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(game, new Game(), new Game()));
        assertTrue(service.findAll().contains(game));
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
    void findByName() {
        when(repository.findById(any(String.class))).thenReturn(Optional.of(game));
        assertTrue(service.findByName(name).isPresent());
        assertEquals(game, service.findByName(name).get());
        Mockito.verify(repository, times(2)).findById(name);
    }

    @Test
    void findByNameEmpty() {
        when(repository.findById(any(String.class))).thenReturn(Optional.empty());
        assertTrue(service.findByName(name).isEmpty());
        Mockito.verify(repository).findById(name);
    }

    @Test
    void save() {
        when(repository.save(any(Game.class))).thenReturn(game);
        assertEquals(game, service.save(game));
        Mockito.verify(repository).save(game);
    }
}
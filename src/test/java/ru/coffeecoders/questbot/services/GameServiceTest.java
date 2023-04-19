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
    private GameRepository gameRepository;
    @InjectMocks
    private GameService gameService;
    private String name;
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
        name = "testName";
    }


    @Test
    void findAll() {
        game.setGameName(name);
        when(gameRepository.findAll()).thenReturn(List.of(game, new Game(), new Game()));
        assertTrue(gameService.findAll().contains(game));
        assertEquals(3, gameService.findAll().size());
        Mockito.verify(gameRepository, times(2)).findAll();
    }

    @Test
    void findAllEmptyList() {
        when(gameRepository.findAll()).thenReturn(List.of());
        assertTrue(gameService.findAll().isEmpty());
        Mockito.verify(gameRepository).findAll();
    }

    @Test
    void findByName() {
        when(gameRepository.findById(any(String.class))).thenReturn(Optional.of(game));
        assertTrue(gameService.findByName(name).isPresent());
        assertEquals(game, gameService.findByName(name).get());
        Mockito.verify(gameRepository, times(2)).findById(name);
    }

    @Test
    void findByNameEmpty() {
        when(gameRepository.findById(any(String.class))).thenReturn(Optional.empty());
        assertTrue(gameService.findByName(name).isEmpty());
        Mockito.verify(gameRepository).findById(name);
    }

    @Test
    void save() {
        when(gameRepository.save(any(Game.class))).thenReturn(game);
        assertEquals(game, gameService.save(game));
        Mockito.verify(gameRepository).save(game);
    }
}

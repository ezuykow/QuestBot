package ru.coffeecoders.questbot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.Player;
import ru.coffeecoders.questbot.repositories.PlayerRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {
    @Mock
    private PlayerRepository playerRepository;
    @InjectMocks
    private PlayerService playerService;
    private Player player;
    private long id;

    @BeforeEach
    void setUp() {
        player = new Player();
        id = 111L;
    }

    @Test
    void findAll() {
        player.setTgUserId(id);
        when(playerRepository.findAll()).thenReturn(List.of(player, new Player(), new Player()));
        assertTrue(playerService.findAll().contains(player));
        assertEquals(3, playerService.findAll().size());
        Mockito.verify(playerRepository, times(2)).findAll();
    }

    @Test
    void findAllEmptyList() {
        when(playerRepository.findAll()).thenReturn(List.of());
        assertTrue(playerService.findAll().isEmpty());
        Mockito.verify(playerRepository).findAll();
    }

    @Test
    void findById() {
        when(playerRepository.findById(any(Long.class))).thenReturn(Optional.of(player));
        assertTrue(playerService.findById(id).isPresent());
        assertEquals(player, playerService.findById(id).get());
        Mockito.verify(playerRepository, times(2)).findById(id);
    }

    @Test
    void findByIdEmpty() {
        when(playerRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertTrue(playerService.findById(id).isEmpty());
        Mockito.verify(playerRepository).findById(id);
    }

    @Test
    void save() {
        when(playerRepository.save(any(Player.class))).thenReturn(player);
        assertEquals(player, playerService.save(player));
        Mockito.verify(playerRepository).save(player);
    }
}
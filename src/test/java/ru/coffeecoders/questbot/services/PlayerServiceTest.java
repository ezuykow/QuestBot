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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository repository;

    @InjectMocks
    private PlayerService service;

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
        when(repository.findAll()).thenReturn(List.of(player, new Player(), new Player()));
        assertTrue(service.findAll().contains(player));
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
        when(repository.findById(anyLong())).thenReturn(Optional.of(player));
        assertTrue(service.findById(id).isPresent());
        assertEquals(player, service.findById(id).get());
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
        when(repository.save(any(Player.class))).thenReturn(player);
        assertEquals(player, service.save(player));
        Mockito.verify(repository).save(player);
    }
}
package ru.coffeecoders.questbot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.Team;
import ru.coffeecoders.questbot.repositories.TeamRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @Mock
    private TeamRepository repository;

    @InjectMocks
    private TeamService service;

    private Team team;
    private String name;
    private long chatId;

    @BeforeEach
    void setUp() {
        team = new Team();
        String name = "Test";
        chatId = 111L;
    }

    @Test
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(team, new Team(), new Team()));
        assertTrue(service.findAll().contains(team));
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
    void findByTeamName() {
        when(repository.findById(name)).thenReturn(Optional.of(team));
        assertTrue(service.findByTeamName(name).isPresent());
        assertEquals(team, service.findByTeamName(name).get());
        verify(repository, times(2)).findById(name);
    }

    @Test
    void findByTeamNameEmpty() {
        when(repository.findById(name)).thenReturn(Optional.empty());
        assertTrue(service.findByTeamName(name).isEmpty());
        verify(repository).findById(name);
    }

    @Test
    void save() {
        when(repository.save(any(Team.class))).thenReturn(team);
        assertEquals(team, service.save(team));
        verify(repository).save(team);
    }

    @Test
    void deleteAllByChatId() {
        service.deleteAllByChatId(chatId);
        verify(repository).deleteAllByChatId(chatId);
    }
}
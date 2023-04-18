package ru.coffeecoders.questbot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.entities.Team;
import ru.coffeecoders.questbot.repositories.TaskRepository;
import ru.coffeecoders.questbot.repositories.TeamRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {
    @Mock
    private TeamRepository teamRepository;
    @InjectMocks
    private TeamService teamService;
    private Team team;
    private String name;

    @BeforeEach
    void setUp() {
        team = new Team();
        String name = "Test";
    }

    @Test
    void findAll() {
        team.setGameName(name);
        when(teamRepository.findAll()).thenReturn(List.of(team, new Team(), new Team()));
        assertTrue(teamService.findAll().contains(team));
        assertEquals(3, teamService.findAll().size());
        Mockito.verify(teamRepository, times(2)).findAll();
    }

    @Test
    void findAllEmptyList() {
        when(teamRepository.findAll()).thenReturn(List.of());
        assertTrue(teamService.findAll().isEmpty());
        Mockito.verify(teamRepository).findAll();
    }

    @Test
    void findByTeamName() {
        when(teamRepository.findById(name)).thenReturn(Optional.of(team));
        assertTrue(teamService.findByTeamName(name).isPresent());
        assertEquals(team, teamService.findByTeamName(name).get());
        Mockito.verify(teamRepository, times(2)).findById(name);
    }

    @Test
    void findByTeamNameEmpty() {
        when(teamRepository.findById(name)).thenReturn(Optional.empty());
        assertTrue(teamService.findByTeamName(name).isEmpty());
        Mockito.verify(teamRepository).findById(name);
    }

    @Test
    void save() {
        when(teamRepository.save(any(Team.class))).thenReturn(team);
        assertEquals(team, teamService.save(team));
        Mockito.verify(teamRepository).save(team);
    }
}
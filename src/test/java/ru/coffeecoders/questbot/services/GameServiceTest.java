package ru.coffeecoders.questbot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.repositories.GameRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock
    private GameRepository repository;

    @InjectMocks
    private GameService service;

    private String name;
    private Game game;
    private int groupId;

    @BeforeEach
    void setUp() {
        game = new Game();
        name = "testName";
        groupId = 1;
    }

    @Test
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(game, new Game(), new Game()));
        assertTrue(service.findAll().contains(game));
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
    void findByName() {
        when(repository.findById(any(String.class))).thenReturn(Optional.of(game));
        assertTrue(service.findByName(name).isPresent());
        assertEquals(game, service.findByName(name).get());
        verify(repository, times(2)).findById(name);
    }

    @Test
    void findByNameEmpty() {
        when(repository.findById(any(String.class))).thenReturn(Optional.empty());
        assertTrue(service.findByName(name).isEmpty());
        verify(repository).findById(name);
    }

    @Test
    void save() {
        when(repository.save(any(Game.class))).thenReturn(game);
        assertEquals(game, service.save(game));
        verify(repository).save(game);
    }

    @Test
    void saveAll() {
        service.saveAll(anyList());
        verify(repository).saveAll(anyList());
    }

    @Test
    void deleteByGameName() {
        service.deleteByGameName(name);
        verify(repository).deleteByGameName(name);
    }

    @Test
    void setGroupIdIfItsDeleted() {
        Game game1 = new Game();
        Game game2 = new Game();
        Game game3 = new Game();
        game1.setGroupsIds(new int[]{1, 2, 3});
        game2.setGroupsIds(new int[]{4, 5, 6});
        game3.setGroupsIds(new int[]{1, 5, 8});
        List<Game> games = List.of(game1, game2, game3);

        when(repository.findAll()).thenReturn(games);
        doAnswer(invocation -> {
            List<Game> argument = invocation.getArgument(0);
            assertEquals(3, argument.size());
            assertEquals(0, argument.get(0).getGroupsIds()[0]);
            assertEquals(5, argument.get(1).getGroupsIds()[1]);
            assertEquals(0, argument.get(2).getGroupsIds()[0]);
            return null;
        }).when(repository).saveAll(anyList());
        service.setGroupIdIfItsDeleted(groupId);
        verify(repository).findAll();
        verify(repository).saveAll(anyList());
    }
}
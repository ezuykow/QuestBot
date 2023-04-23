package ru.coffeecoders.questbot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.AdminChatMembers;
import ru.coffeecoders.questbot.repositories.AdminChatMembersRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

/**
 * @author Anatoliy Shikin
 */
@ExtendWith(MockitoExtension.class)
class AdminChatMembersServiceTest {

    @Mock
    private AdminChatMembersRepository repository;

    @InjectMocks
    private AdminChatMembersService service;

    private AdminChatMembers members;
    private long id;

    @BeforeEach
    void setUp() {
        members = new AdminChatMembers();
        id = 111L;
    }

    @Test
    void findAll() {
        when(repository.findAll()).thenReturn(List.of(members, new AdminChatMembers(), new AdminChatMembers()));
        assertEquals(3, service.findAll().size());
        assertTrue(service.findAll().contains(members));
        Mockito.verify(repository, times(2)).findAll();
    }

    @Test
    void findAllEmptyListTest() {
        when(repository.findAll()).thenReturn(List.of());
        assertTrue(service.findAll().isEmpty());
        Mockito.verify(repository).findAll();
    }

    @Test
    void findByChatId() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(members));
        assertTrue(service.findByChatId(id).isPresent());
        assertEquals(members, service.findByChatId(id).get());
        Mockito.verify(repository, times(2)).findById(id);
    }

    @Test
    void findByChatIdEmpty() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertTrue(service.findByChatId(id).isEmpty());
        Mockito.verify(repository).findById(id);
    }

    @Test
    void save() {
        service.save(members);
        Mockito.verify(repository).save(members);
    }

    @Test
    void deleteByChatId() {
        service.deleteByChatId(id);
        Mockito.verify(repository).deleteById(id);
    }
}
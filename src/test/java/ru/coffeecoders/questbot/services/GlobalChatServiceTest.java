package ru.coffeecoders.questbot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.repositories.GlobalChatRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalChatServiceTest {

    @Mock
    private GlobalChatRepository repository;

    @InjectMocks
    private GlobalChatService service;

    private GlobalChat globalChat;
    private long id;

    @BeforeEach
    void setUp() {
        globalChat = new GlobalChat();
        id = 111L;
    }

    @Test
    void findAll() {
        globalChat.setTgChatId(id);
        when(repository.findAll()).thenReturn(List.of(globalChat, new GlobalChat(), new GlobalChat()));
        assertEquals(3, repository.findAll().size());
        assertTrue(service.findAll().contains(globalChat));
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
        when(repository.findById(anyLong())).thenReturn(Optional.of(globalChat));
        assertTrue(service.findById(id).isPresent());
        assertEquals(globalChat, service.findById(id).get());
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
        when(repository.save(any(GlobalChat.class))).thenReturn(globalChat);
        assertEquals(globalChat, service.save(globalChat));
        Mockito.verify(repository).save(globalChat);
    }
}
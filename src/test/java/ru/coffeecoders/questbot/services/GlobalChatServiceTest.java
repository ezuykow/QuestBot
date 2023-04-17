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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalChatServiceTest {
    @Mock
    private GlobalChatRepository globalChatRepository;
    @InjectMocks
    private GlobalChatService globalChatService;
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
        when(globalChatRepository.findAll()).thenReturn(List.of(globalChat, new GlobalChat(), new GlobalChat()));
        assertEquals(3, globalChatRepository.findAll().size());
        assertTrue(globalChatService.findAll().contains(globalChat));
        Mockito.verify(globalChatRepository, times(2)).findAll();
    }

    @Test
    void findAllEmptyList() {
        when(globalChatRepository.findAll()).thenReturn(List.of());
        assertTrue(globalChatService.findAll().isEmpty());
        Mockito.verify(globalChatRepository).findAll();
    }

    @Test
    void findById() {
        when(globalChatRepository.findByTgChatId(any(Long.class))).thenReturn(Optional.of(globalChat));
        assertTrue(globalChatService.findById(id).isPresent());
        assertEquals(globalChat, globalChatService.findById(id).get());
        Mockito.verify(globalChatRepository, times(2)).findByTgChatId(id);
    }

    @Test
    void findByIdEmpty() {
        when(globalChatRepository.findByTgChatId(any(Long.class))).thenReturn(Optional.empty());
        assertTrue(globalChatService.findById(id).isEmpty());
        Mockito.verify(globalChatRepository).findByTgChatId(id);
    }

    @Test
    void save() {
        when(globalChatRepository.save(any(GlobalChat.class))).thenReturn(globalChat);
        assertEquals(globalChat, globalChatService.save(globalChat));
        Mockito.verify(globalChatRepository).save(globalChat);
    }
}
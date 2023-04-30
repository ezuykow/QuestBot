package ru.coffeecoders.questbot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.repositories.AdminChatRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminChatServiceTest {

    @Mock
    private AdminChatRepository repository;

    @InjectMocks
    private AdminChatService service;

    private long id;
    private AdminChat chat;

    @BeforeEach
    void setUp() {
        id = 123L;
        chat = new AdminChat();
    }

    @Test
    void findAllTest() {
        when(repository.findAll()).thenReturn(List.of(chat, new AdminChat(), new AdminChat()));
        assertEquals(3, service.findAll().size());
        assertTrue(service.findAll().contains(chat));
        verify(repository, times(2)).findAll();
    }

    @Test
    void findAllEmptyListTest() {
        when(repository.findAll()).thenReturn(List.of());
        assertTrue(service.findAll().isEmpty());
        verify(repository).findAll();
    }

    @Test
    void findById() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(chat));
        assertTrue(service.findById(id).isPresent());
        assertEquals(chat, service.findById(id).get());
        verify(repository, times(2)).findById(id);
    }

    @Test
    void findByIdEmpty() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertTrue(service.findById(id).isEmpty());
        verify(repository).findById(id);
    }

    @Test
    void saveTest() {
        when(repository.save(any(AdminChat.class))).thenReturn(chat);
        assertEquals(chat, service.save(chat));
        verify(repository).save(chat);
    }

    @Test
    void delete() {
        service.delete(chat);
        verify(repository).delete(chat);
    }

    @Test
    void deleteByChatId() {
        service.deleteByChatId(id);
        verify(repository).deleteById(id);
    }
}
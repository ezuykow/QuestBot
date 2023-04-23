package ru.coffeecoders.questbot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.Admin;
import ru.coffeecoders.questbot.repositories.AdminRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminRepository repository;

    @InjectMocks
    private AdminService service;

    private long id;
    private Admin admin;

    @BeforeEach
    void setUp() {
        id = 123L;
        admin = new Admin();
    }

    @Test
    void findAllTest() {
        when(repository.findAll()).thenReturn(List.of(admin, new Admin(), new Admin()));
        assertEquals(3, service.findAll().size());
        assertTrue(service.findAll().contains(admin));
        Mockito.verify(repository, times(2)).findAll();
    }

    @Test
    void findAllEmptyListTest() {
        when(repository.findAll()).thenReturn(List.of());
        assertTrue(service.findAll().isEmpty());
        Mockito.verify(repository).findAll();
    }

    @Test
    void findById() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(admin));
        assertTrue(service.findById(id).isPresent());
        assertEquals(admin, service.findById(id).get());
        Mockito.verify(repository, times(2)).findById(id);
    }

    @Test
    void findByIdEmpty() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        assertTrue(service.findById(id).isEmpty());
        Mockito.verify(repository).findById(id);
    }

    @Test
    void saveTest() {
        when(repository.save(any(Admin.class))).thenReturn(admin);
        assertEquals(admin, service.save(admin));
        Mockito.verify(repository).save(admin);
    }

    @Test
    void deleteAll() {
        service.deleteAll(List.of(admin, new Admin(), new Admin()));
        Mockito.verify(repository,times(1)).deleteAll(anyList());
    }
}
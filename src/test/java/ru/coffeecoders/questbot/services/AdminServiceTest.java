package ru.coffeecoders.questbot.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.Admin;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.repositories.AdminRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
        when(repository.findById(anyLong())).thenReturn(Optional.of(admin));
        assertTrue(service.findById(id).isPresent());
        assertEquals(admin, service.findById(id).get());
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
        when(repository.save(any(Admin.class))).thenReturn(admin);
        assertEquals(admin, service.save(admin));
        verify(repository).save(admin);
    }

    @Test
    void deleteAll() {
        service.deleteAll(List.of(admin, new Admin(), new Admin()));
        verify(repository).deleteAll(anyList());
    }

    @Test
    void getOwner() {
        List<Admin> admins = new ArrayList<>();
        admins.add(new Admin(111, true, null));
        admins.add(new Admin(123, false, null));
        when(repository.findAll()).thenReturn(admins);
        assertEquals(admins.get(0), service.getOwner());
    }

    @Test
    void getOwnerEmpty() {
        List<Admin> admins = new ArrayList<>();
        admins.add(new Admin(111, false, null));
        admins.add(new Admin(123, false, null));
        when(repository.findAll()).thenReturn(admins);
        assertThrows(RuntimeException.class, () -> service.getOwner());
    }

    @Test
    void deleteUselessAdmins() {
        List<Admin> admins = new ArrayList<>();
        admins.add(new Admin(111, false, null));
        admins.add(new Admin(123, true, null));
        admins.add(new Admin(123, false, Set.of(new AdminChat())));
        when(repository.findAll()).thenReturn(admins);
        service.deleteUselessAdmins();
        verify(repository).deleteAll(List.of(admins.get(0)));
    }
}
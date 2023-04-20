package ru.coffeecoders.questbot.validators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.Admin;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.services.AdminChatService;
import ru.coffeecoders.questbot.services.AdminService;
import ru.coffeecoders.questbot.services.GlobalChatService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatAndUserValidatorTest {
    @Mock
    private AdminService adminService;
    @Mock
    private AdminChatService adminChatService;
    @Mock
    private GlobalChatService globalChatService;
    @InjectMocks
    ChatAndUserValidator validator;

    @Test
    public void isAdminTest() {
        Admin admin = new Admin();
        when(adminService.findById(1L)).thenReturn(Optional.of(admin));
        assertTrue(validator.isAdmin(1L));
    }

    @Test
    public void isNotAdminTest() {
        when(adminService.findById(1L)).thenReturn(Optional.empty());
        assertFalse(validator.isAdmin(1L));
    }

    @Test
    public void isAdminChatTest() {
        AdminChat adminChat = new AdminChat();
        when(adminChatService.findById(1L)).thenReturn(Optional.of(adminChat));
        assertTrue(validator.isAdminChat(1L));
    }

    @Test
    public void isNotAdminChatTest() {
        when(adminChatService.findById(1L)).thenReturn(Optional.empty());
        assertFalse(validator.isAdminChat(1L));
    }

    @Test
    public void isGlobalChatTest() {
        GlobalChat chat = new GlobalChat();
        when(globalChatService.findById(1L)).thenReturn(Optional.of(chat));
        assertTrue(validator.isGlobalChat(1L));
    }

    @Test
    public void isNotGlobalChatTest() {
        when(globalChatService.findById(1L)).thenReturn(Optional.empty());
        assertFalse(validator.isGlobalChat(1L));
    }

}
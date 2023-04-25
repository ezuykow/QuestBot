package ru.coffeecoders.questbot.actions.commands;

import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.Admin;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.entities.AdminChatMembers;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.keyboards.PromoteOrDemoteUserKeyboard;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.services.AdminChatMembersService;
import ru.coffeecoders.questbot.services.AdminChatService;
import ru.coffeecoders.questbot.services.AdminService;
import ru.coffeecoders.questbot.services.GlobalChatService;
import ru.coffeecoders.questbot.validators.ChatAndUserValidator;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerCommandsActionsTest {
    @Mock
    private ChatAndUserValidator validator;
    @Mock
    private AdminService adminService;
    @Mock
    private GlobalChatService globalChatService;
    @Mock
    private AdminChatService adminChatService;
    @Mock
    private AdminChatMembersService adminChatMembersService;
    @Mock
    private AdminChat adminChat;

    @Mock
    private Messages messages;
    @Mock
    private MessageSender msgSender;
    @Mock
    private Admin owner;
    @Mock
    private AdminChatMembers members;
    @Mock
    private InlineKeyboardMarkup keyboard;

    long chatId = 1L;

    @InjectMocks
    OwnerCommandsActions actions;

    @BeforeEach
    void setUp() {

    }

    @Test
    void validateAndPerformStartCmdIfChatNotAddedTest() {
        when(validator.chatNotAdded(chatId)).thenReturn(true);
        when(messages.welcome()).thenReturn("Привет, я QuestBot) Если вы хотите назначить этот чат админским -\n" +
                "    выберите соответсвующую команду в меню");
        actions.validateAndPerformStartCmd(chatId);
        verify(globalChatService).save(new GlobalChat(chatId));
        verify(msgSender).send(chatId, "Привет, я QuestBot) Если вы хотите назначить этот чат админским -\n" +
                "    выберите соответсвующую команду в меню");
    }

    @Test
    void validateAndPerformStartCmdIfChatAddedTest() {
        when(validator.chatNotAdded(chatId)).thenReturn(false);
        when(messages.startCmdFailed()).thenReturn("Этот чат уже добавлен в систему");
        actions.validateAndPerformStartCmd(chatId);
        verify(msgSender).send(chatId, "Этот чат уже добавлен в систему");
    }

    @Test
    void validateAndPerformAdminOnCmdIfGlobalChatTest() {
        long userId = 12L;
        when(validator.isGlobalChat(chatId)).thenReturn(true);
        when(adminService.getOwner()).thenReturn(owner);
        when(messages.chatIsAdminNow()).thenReturn("Теперь этот чат - администраторский");
        when(owner.getTgAdminUserId()).thenReturn(userId);
        actions.validateAndPerformAdminOnCmd(chatId);
        verify(msgSender).send(chatId, "Теперь этот чат - администраторский");
        verify(globalChatService).deleteById(chatId);
        verify(adminChatService).save(new AdminChat(chatId, Collections.singleton(owner)));
        verify(adminChatMembersService).save(new AdminChatMembers(chatId, new long[]{userId}));
    }

    @Test
    void validateAndPerformAdminOnCmdIfNotGlobalChatTest() {
        when(validator.isGlobalChat(chatId)).thenReturn(false);
        when(messages.adminOnCmdFailed()).thenReturn("Этот чат уже администраторский");
        actions.validateAndPerformAdminOnCmd(chatId);
        verify(msgSender).send(chatId, "Этот чат уже администраторский");
    }

    @Test
    void validateAndPerformAdminOffCmdIfAdminChatTest() {
        when(validator.isAdminChat(chatId)).thenReturn(true);
        when(messages.chatIsGlobalNow()).thenReturn("Теперь этот чат - не администраторский");
        actions.validateAndPerformAdminOffCmd(chatId);
        verify(adminChatService).deleteByChatId(chatId);
        verify(globalChatService).save(new GlobalChat(chatId));
        verify(adminChatMembersService).deleteByChatId(chatId);
        verify(adminService).deleteUselessAdmins();
        verify(msgSender).send(chatId, "Теперь этот чат - не администраторский");
    }

    @Test
    void validateAndPerformAdminOffCmdIfNotAdminChatTest() {
        when(validator.isAdminChat(chatId)).thenReturn(false);
        when(messages.chatIsNotAdmin()).thenReturn("Этот чат не администраторский");
        actions.validateAndPerformAdminOffCmd(chatId);
        verify(msgSender).send(chatId, "Этот чат не администраторский");
    }


    @Test
    void validateAndPerformPromoteCmdIfAdminChatTest() {
        when(validator.isAdminChat(chatId)).thenReturn(true);
        when(adminChatMembersService.findByChatId(chatId)).thenReturn(Optional.of(members));
        when(members.getMembers()).thenReturn(new long[]{1, 2, 3});
        when(adminChatService.findById(chatId)).thenReturn(Optional.of(adminChat));
        when(adminChat.getAdmins()).thenReturn(Set.of(new Admin(3)));
        when(msgSender.getChatMember(chatId, 1)).thenReturn(new User(1L));
        when(msgSender.getChatMember(chatId, 1)).thenReturn(new User(2L));
        when(messages.promote()).thenReturn("Назначить администратором бота:");
        try (MockedStatic<PromoteOrDemoteUserKeyboard> theMock = mockStatic(PromoteOrDemoteUserKeyboard.class)) {
            theMock.when(() -> PromoteOrDemoteUserKeyboard.createKeyboard(anySet(), anyString())).thenReturn(keyboard);
            actions.validateAndPerformPromoteCmd(chatId);
            verify(msgSender).send(chatId, "Назначить администратором бота:", keyboard);
        }

    }

    @Test
    void validateAndPerformPromoteCmdIfAdminChatButHaventSimplyUserTest() {
        when(validator.isAdminChat(chatId)).thenReturn(true);
        when(adminChatMembersService.findByChatId(chatId)).thenReturn(Optional.of(members));
        when(members.getMembers()).thenReturn(new long[]{3});
        when(adminChatService.findById(chatId)).thenReturn(Optional.of(adminChat));
        when(adminChat.getAdmins()).thenReturn(Set.of(new Admin(3)));
        when(messages.emptyPromotionList()).thenReturn("Некого назначать админом");
        actions.validateAndPerformPromoteCmd(chatId);
        verify(msgSender).send(chatId, "Некого назначать админом");
    }

    @Test
    void validateAndPerformPromoteCmdIfNotAdminChatTest() {
        when(validator.isAdminChat(chatId)).thenReturn(false);
        when(messages.chatIsNotAdmin()).thenReturn("Этот чат не администраторский");
        actions.validateAndPerformPromoteCmd(chatId);
        verify(msgSender).send(chatId, "Этот чат не администраторский");
    }

    @Test
    void validateAndPerformDemoteCmdIfChatAdminTest() {
        Admin admin = new Admin(3);
        when(validator.isAdminChat(chatId)).thenReturn(true);
        when(adminChatService.findById(chatId)).thenReturn(Optional.of(adminChat));
        when(adminChat.getAdmins()).thenReturn(Set.of(admin, new Admin(1)));
        when(adminService.getOwner()).thenReturn(admin);
        when(msgSender.getChatMember(chatId, 1)).thenReturn(new User(1L));
        when(messages.demote()).thenReturn("Разжаловать администратора бота:");
        try (MockedStatic<PromoteOrDemoteUserKeyboard> theMock = mockStatic(PromoteOrDemoteUserKeyboard.class)) {
            theMock.when(() -> PromoteOrDemoteUserKeyboard.createKeyboard(anySet(), anyString())).thenReturn(keyboard);
            actions.validateAndPerformDemoteCmd(chatId);
            verify(msgSender).send(chatId, "Разжаловать администратора бота:", keyboard);
        }

    }

    @Test
    void validateAndPerformDemoteCmdIfChatAdminButHaventAdminTest() {
        Admin admin = new Admin(3);
        when(validator.isAdminChat(chatId)).thenReturn(true);
        when(adminChatService.findById(chatId)).thenReturn(Optional.of(adminChat));
        when(adminChat.getAdmins()).thenReturn(Set.of(admin));
        when(adminService.getOwner()).thenReturn(admin);
        when(messages.emptyPromotionList()).thenReturn("Некого назначать админом");
        actions.validateAndPerformDemoteCmd(chatId);
        verify(msgSender).send(chatId, "Некого назначать админом");
    }

    @Test
    void validateAndPerformDemoteCmdIfChatNotAdmin() {
        when(validator.isAdminChat(chatId)).thenReturn(false);
        when(messages.chatIsNotAdmin()).thenReturn("Этот чат не администраторский");
        actions.validateAndPerformDemoteCmd(chatId);
        verify(msgSender).send(chatId, "Этот чат не администраторский");
    }
}
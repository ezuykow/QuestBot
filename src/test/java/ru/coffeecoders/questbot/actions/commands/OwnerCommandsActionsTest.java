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

    private final long chatId = 1L;
    private final String msgNotAdminChat = "Этот чат не администраторский";
    private final String msgHaventAdmins = "Некого назначать админом";

    @InjectMocks
    private OwnerCommandsActions actions;

    @BeforeEach
    void setUp() {

    }

    @Test
    void validateAndPerformStartCmdIfChatNotAddedTest() {
        String msg = "Привет, я QuestBot) Если вы хотите назначить этот чат админским -\n" +
                "    выберите соответсвующую команду в меню";
        when(validator.chatNotAdded(chatId)).thenReturn(true);
        when(messages.welcome()).thenReturn(msg);
        actions.validateAndPerformStartCmd(chatId);
        verify(globalChatService).save(new GlobalChat(chatId));
        verify(msgSender).send(chatId, msg);
    }

    @Test
    void validateAndPerformStartCmdIfChatAddedTest() {
        String msg = "Этот чат уже добавлен в систему";
        when(validator.chatNotAdded(chatId)).thenReturn(false);
        when(messages.startCmdFailed()).thenReturn(msg);
        actions.validateAndPerformStartCmd(chatId);
        verify(msgSender).send(chatId, msg);
    }

    @Test
    void validateAndPerformAdminOnCmdIfGlobalChatTest() {
        String msg = "Теперь этот чат - администраторский";
        long userId = 12L;
        when(validator.isGlobalChat(chatId)).thenReturn(true);
        when(adminService.getOwner()).thenReturn(owner);
        when(messages.chatIsAdminNow()).thenReturn(msg);
        when(owner.getTgAdminUserId()).thenReturn(userId);
        actions.validateAndPerformAdminOnCmd(chatId);
        verify(msgSender).send(chatId, msg);
        verify(globalChatService).deleteById(chatId);
        verify(adminChatService).save(new AdminChat(chatId, Collections.singleton(owner)));
        verify(adminChatMembersService).save(new AdminChatMembers(chatId, new long[]{userId}));
    }

    @Test
    void validateAndPerformAdminOnCmdIfNotGlobalChatTest() {
        String msg = "Этот чат уже администраторский";
        when(validator.isGlobalChat(chatId)).thenReturn(false);
        when(messages.adminOnCmdFailed()).thenReturn(msg);
        actions.validateAndPerformAdminOnCmd(chatId);
        verify(msgSender).send(chatId, msg);
    }

    @Test
    void validateAndPerformAdminOffCmdIfAdminChatTest() {
        String msg = "Теперь этот чат - не администраторский";
        when(validator.isAdminChat(chatId)).thenReturn(true);
        when(messages.chatIsGlobalNow()).thenReturn(msg);
        actions.validateAndPerformAdminOffCmd(chatId);
        verify(adminChatService).deleteByChatId(chatId);
        verify(globalChatService).save(new GlobalChat(chatId));
        verify(adminChatMembersService).deleteByChatId(chatId);
        verify(adminService).deleteUselessAdmins();
        verify(msgSender).send(chatId, msg);
    }

    @Test
    void validateAndPerformAdminOffCmdIfNotAdminChatTest() {
        when(validator.isAdminChat(chatId)).thenReturn(false);
        when(messages.chatIsNotAdmin()).thenReturn(msgNotAdminChat);
        actions.validateAndPerformAdminOffCmd(chatId);
        verify(msgSender).send(chatId, msgNotAdminChat);
    }


    @Test
    void validateAndPerformPromoteCmdIfAdminChatTest() {
        String msg = "Назначить администратором бота:";
        when(validator.isAdminChat(chatId)).thenReturn(true);
        when(adminChatMembersService.findByChatId(chatId)).thenReturn(Optional.of(members));
        when(members.getMembers()).thenReturn(new long[]{1, 2, 3});
        when(adminChatService.findById(chatId)).thenReturn(Optional.of(adminChat));
        when(adminChat.getAdmins()).thenReturn(Set.of(new Admin(3)));
        when(msgSender.getChatMember(chatId, 1)).thenReturn(new User(1L));
        when(msgSender.getChatMember(chatId, 1)).thenReturn(new User(2L));
        when(messages.promote()).thenReturn(msg);
        try (MockedStatic<PromoteOrDemoteUserKeyboard> theMock = mockStatic(PromoteOrDemoteUserKeyboard.class)) {
            theMock.when(() -> PromoteOrDemoteUserKeyboard.createKeyboard(anySet(), anyString())).thenReturn(keyboard);
            actions.validateAndPerformPromoteCmd(chatId);
            verify(msgSender).send(chatId, msg, keyboard);
        }

    }

    @Test
    void validateAndPerformPromoteCmdIfAdminChatButHaventSimplyUserTest() {
        when(validator.isAdminChat(chatId)).thenReturn(true);
        when(adminChatMembersService.findByChatId(chatId)).thenReturn(Optional.of(members));
        when(members.getMembers()).thenReturn(new long[]{3});
        when(adminChatService.findById(chatId)).thenReturn(Optional.of(adminChat));
        when(adminChat.getAdmins()).thenReturn(Set.of(new Admin(3)));
        when(messages.emptyPromotionList()).thenReturn(msgHaventAdmins);
        actions.validateAndPerformPromoteCmd(chatId);
        verify(msgSender).send(chatId, msgHaventAdmins);
    }

    @Test
    void validateAndPerformPromoteCmdIfNotAdminChatTest() {
        when(validator.isAdminChat(chatId)).thenReturn(false);
        when(messages.chatIsNotAdmin()).thenReturn(msgNotAdminChat);
        actions.validateAndPerformPromoteCmd(chatId);
        verify(msgSender).send(chatId, msgNotAdminChat);
    }

    @Test
    void validateAndPerformDemoteCmdIfChatAdminTest() {
        String msg = "Разжаловать администратора бота:";
        Admin admin = new Admin(3);
        when(validator.isAdminChat(chatId)).thenReturn(true);
        when(adminChatService.findById(chatId)).thenReturn(Optional.of(adminChat));
        when(adminChat.getAdmins()).thenReturn(Set.of(admin, new Admin(1)));
        when(adminService.getOwner()).thenReturn(admin);
        when(msgSender.getChatMember(chatId, 1)).thenReturn(new User(1L));
        when(messages.demote()).thenReturn(msg);
        try (MockedStatic<PromoteOrDemoteUserKeyboard> theMock = mockStatic(PromoteOrDemoteUserKeyboard.class)) {
            theMock.when(() -> PromoteOrDemoteUserKeyboard.createKeyboard(anySet(), anyString())).thenReturn(keyboard);
            actions.validateAndPerformDemoteCmd(chatId);
            verify(msgSender).send(chatId, msg, keyboard);
        }

    }

    @Test
    void validateAndPerformDemoteCmdIfChatAdminButHaventAdminTest() {
        Admin admin = new Admin(3);
        when(validator.isAdminChat(chatId)).thenReturn(true);
        when(adminChatService.findById(chatId)).thenReturn(Optional.of(adminChat));
        when(adminChat.getAdmins()).thenReturn(Set.of(admin));
        when(adminService.getOwner()).thenReturn(admin);
        when(messages.emptyPromotionList()).thenReturn(msgHaventAdmins);
        actions.validateAndPerformDemoteCmd(chatId);
        verify(msgSender).send(chatId, msgHaventAdmins);
    }

    @Test
    void validateAndPerformDemoteCmdIfChatNotAdmin() {
        when(validator.isAdminChat(chatId)).thenReturn(false);
        when(messages.chatIsNotAdmin()).thenReturn(msgNotAdminChat);
        actions.validateAndPerformDemoteCmd(chatId);
        verify(msgSender).send(chatId, msgNotAdminChat);
    }
}
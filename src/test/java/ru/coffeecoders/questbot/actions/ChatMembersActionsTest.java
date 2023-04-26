package ru.coffeecoders.questbot.actions;

import com.pengrad.telegrambot.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.Admin;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.entities.AdminChatMembers;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.services.AdminChatMembersService;
import ru.coffeecoders.questbot.services.AdminChatService;
import ru.coffeecoders.questbot.services.AdminService;
import ru.coffeecoders.questbot.validators.ChatAndUserValidator;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatMembersActionsTest {
    @Mock
    private AdminChatMembersService adminChatMembersService;
    @Mock
    private AdminChatService adminChatService;
    @Mock
    private AdminService adminService;
    @Mock
    private ChatAndUserValidator validator;
    @Mock
    private MessageSender msgSender;
    @Mock
    private Messages messages;
    @Mock
    private User member;
    @Mock
    private AdminChat adminChat;


    private final long chatId = 1L;
    private final long userId = 12L;
    private final String name = "Name";
    private final String surname = "Surname";
    private AdminChatMembers adminChatMembers;
    private final long[] chatMembers = new long[]{1, 12};


    @InjectMocks
    private ChatMembersActions actions;

    @BeforeEach
    void setUp() {
        when(member.firstName()).thenReturn(name);
        when(member.lastName()).thenReturn(surname);
    }


    @Test
    void newChatMemberInAdminChatTest() {
        adminChatMembers = new AdminChatMembers(chatId, chatMembers);
        when(validator.isAdminChat(chatId)).thenReturn(true);
        when(messages.welcomeAdminSuffix()).thenReturn(", приветствуем!");
        when(messages.welcomeAdminPrefix()).thenReturn("Новый помощник - ");
        when(adminChatMembersService.findByChatId(chatId)).thenReturn(Optional.of(adminChatMembers));
        when(member.id()).thenReturn(userId);
        actions.newChatMember(member, chatId);
        verify(msgSender).send(chatId, "Новый помощник - " + name + " " + surname + ", приветствуем!");
        verify(adminChatMembersService).save(adminChatMembers);
    }

    @Test
    void newChatMemberInGlobalChatTest() {
        when(validator.isGlobalChat(chatId)).thenReturn(true);
        when(messages.welcomeSuffix()).thenReturn("! Приятного времяпрепровождения и удачи на игре!");
        when(messages.welcomePrefix()).thenReturn("Поприветствуем нового игрока - ");
        actions.newChatMember(member, chatId);
        verify(msgSender).send(chatId, "Поприветствуем нового игрока - " + name + " " + surname
                + "! Приятного времяпрепровождения и удачи на игре!");
    }

    @Test
    void leftChatMemberInAdminChatTest() {
        adminChatMembers = new AdminChatMembers(chatId, chatMembers);
        Set<Admin> adminSet = new HashSet<>();
        adminSet.add(new Admin(userId));
        when(validator.isAdminChat(chatId)).thenReturn(true);
        when(member.id()).thenReturn(userId);
        when(adminChatMembersService.findByChatId(chatId)).thenReturn(Optional.of(adminChatMembers));
        when(adminChat.getAdmins()).thenReturn(adminSet);
        when(adminChatService.findById(chatId)).thenReturn(Optional.of(adminChat));
        when(messages.byeAdmin()).thenReturn(" покинул нас...");
        actions.leftChatMember(member, chatId);
        verify(adminChatService).save(adminChat);
        verify(adminChatMembersService).save(adminChatMembers);
        verify(msgSender).send(chatId, name + " " + surname + " покинул нас...");
    }

    @Test
    void leftChatMemberInGlobalChatTest() {
        when(validator.isGlobalChat(chatId)).thenReturn(true);
        when(messages.byePrefix()).thenReturn("Пока, ");
        when(messages.byeSuffix()).thenReturn(", надеемся вы еще вернетесь!");
        actions.leftChatMember(member, chatId);
        msgSender.send(chatId, "Пока, " + name + " " + surname + ", надеемся вы еще вернетесь!");
    }
}
package ru.coffeecoders.questbot.managers;


import com.pengrad.telegrambot.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.logs.LogSender;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.services.AdminChatService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author ezuykow
 */
@ExtendWith(MockitoExtension.class)
class BlockingManagerTest {

    @Mock
    private AdminChatService adminChatService;
    @Mock
    private MessageSender msgSender;
    @Mock
    private LogSender logger;

    @InjectMocks
    private BlockingManager blockingManager;

    @Test
    public void shouldCallSwitchBlockerWithEqualsParametersThenCalledBlockAdminByChatId() {
        doNothing().when(logger).warn(anyString());

        long chatId = 1;
        long userId = 1;
        AdminChat chat = new AdminChat();

        when(adminChatService.findById(chatId)).thenReturn(Optional.of(chat));
        when(adminChatService.save(chat)).thenReturn(chat);
        when(msgSender.getChatMember(chatId, userId)).thenReturn(new User(userId));
        doNothing().when(msgSender).send(eq(chatId), anyString());

        blockingManager.blockAdminChatByAdmin(chatId, userId, "cause");
        verify(msgSender).send(eq(chatId), anyString());
    }

    @Test
    public void shouldCallSwitchBlockerWithEqualsParametersThenCalledUnblockAdminChat() {
        long chatId = 2;
        AdminChat chat = new AdminChat();

        when(adminChatService.findById(chatId)).thenReturn(Optional.of(chat));
        when(adminChatService.save(chat)).thenReturn(chat);

        blockingManager.unblockAdminChat(chatId, "cause");
        verify(msgSender).send(eq(chatId), anyString());
    }

    @Test
    public void shouldReturnBlockedAdminIdWheCalledGetBlockedAdminId() {
        long chatId = 3;
        long actualBlockedAdminId = 10;
        AdminChat chat = new AdminChat(chatId, actualBlockedAdminId, null);

        when(adminChatService.findById(chatId)).thenReturn(Optional.of(chat));

        long expectedAdminId = blockingManager.getBlockedAdminId(chatId);
        Assertions.assertEquals(expectedAdminId, actualBlockedAdminId);
    }
}
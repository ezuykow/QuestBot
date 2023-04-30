package ru.coffeecoders.questbot.managers;

import com.pengrad.telegrambot.model.ChatPermissions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.AdminChatMembers;
import ru.coffeecoders.questbot.logs.LogSender;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.services.AdminChatMembersService;
import ru.coffeecoders.questbot.validators.ChatAndUserValidator;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author ezuykow
 */
@ExtendWith(MockitoExtension.class)
class RestrictingManagerTest {

    @Mock
    private AdminChatMembersService adminChatMembersService;
    @Mock
    private ChatAndUserValidator validator;
    @Mock
    private MessageSender msgSender;
    @Mock
    private LogSender logger;

    @InjectMocks
    private RestrictingManager restrictingManager;

    private long chatId;
    private long initiatorId;
    private AdminChatMembers chat;

    @BeforeEach
    public void setUp() {
        long ownerId = 2L;
        chatId = 10L;
        initiatorId = 1L;
        chat = new AdminChatMembers(chatId, new long[]{initiatorId, ownerId, 3L, 4L});

        doNothing().when(logger).warn(anyString());
        when(adminChatMembersService.findByChatId(chatId)).thenReturn(Optional.of(chat));
        doNothing().when(msgSender)
                .sendRestrictChatMember(eq(chatId), anyLong(), any(ChatPermissions.class));
        when(validator.isOwner(anyLong())).thenReturn(false);
        when(validator.isOwner(ownerId)).thenReturn(true);
    }

    @Test
    public void shouldRestrictChatMembers() {
        restrictingManager.restrictMembers(chatId, initiatorId);
        verify(msgSender, times(chat.getMembers().length - 2))
                .sendRestrictChatMember(eq(chatId), anyLong(), any(ChatPermissions.class));
    }

    @Test
    public void shouldUnrestrictChatMembers() {
        restrictingManager.unRestrictMembers(chatId);
        verify(msgSender, times(chat.getMembers().length - 1))
                .sendRestrictChatMember(eq(chatId), anyLong(), any(ChatPermissions.class));
    }
}
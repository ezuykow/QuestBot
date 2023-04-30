package ru.coffeecoders.questbot.managers;

import com.pengrad.telegrambot.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.actions.ChatMembersActions;
import ru.coffeecoders.questbot.logs.LogSender;
import ru.coffeecoders.questbot.models.ExtendedUpdate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author ezuykow
 */
@ExtendWith(MockitoExtension.class)
class ChatMembersManagerTest {

    @Mock
    private ChatMembersActions chatMembersActions;
    @Mock
    private LogSender logger;
    @Mock
    private ExtendedUpdate update;

    @InjectMocks
    private ChatMembersManager chatMembersManager;

    private long chatId;
    private long userId;
    private User user;

    @BeforeEach
    public void setUp() {
        doNothing().when(logger).warn(anyString());

        chatId = 1L;
        userId = 1L;
        user = new User(userId);
        when(update.getUpdatedMemberChatId()).thenReturn(chatId);
        when(update.getUpdatedMemberUser()).thenReturn(user);
    }

    @Test
    public void shouldCallNewChatMembersMethodThenCalled() {
        doNothing().when(chatMembersActions).newChatMember(user, chatId);
        chatMembersManager.manageChatMembers(update, ExtendedUpdate.UpdateType.NEW_CHAT_MEMBER);
        verify(chatMembersActions).newChatMember(user, chatId);
    }

    @Test
    public void shouldCallLeftChatMembersMethodThenCalled() {
        doNothing().when(chatMembersActions).leftChatMember(user, chatId);
        chatMembersManager.manageChatMembers(update, ExtendedUpdate.UpdateType.LEFT_CHAT_MEMBER);
        verify(chatMembersActions).leftChatMember(user, chatId);
    }
}
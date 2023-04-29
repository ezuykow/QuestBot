package ru.coffeecoders.questbot.keyboards;

import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PromoteOrDemoteUserKeyboardTest {
    @Mock
    private User user1;
    @Mock
    private User user2;

    @Test
    void createKeyboard() {
        String dataPrefix = "prefix";
        Set<User> users = Set.of(user1, user2);
        when(user1.lastName()).thenReturn("lastName");
        assertEquals(PromoteOrDemoteUserKeyboard.createKeyboard(users, dataPrefix).getClass(),
                InlineKeyboardMarkup.class);
    }
}
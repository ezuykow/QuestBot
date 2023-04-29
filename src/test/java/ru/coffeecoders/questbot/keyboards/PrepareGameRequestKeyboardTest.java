package ru.coffeecoders.questbot.keyboards;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class PrepareGameRequestKeyboardTest {

    @Test
    void createKeyboardTest() {
        long adminId = 1L;
        String gameName1 = "gameName1";
        String gameName2 = "gameName2";
        List<String> gameNames = List.of(gameName1, gameName2);
        assertEquals(PrepareGameRequestKeyboard.createKeyboard(gameNames, adminId).getClass(),
                InlineKeyboardMarkup.class);
    }
}
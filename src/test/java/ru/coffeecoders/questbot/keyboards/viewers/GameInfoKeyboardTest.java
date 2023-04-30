package ru.coffeecoders.questbot.keyboards.viewers;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameInfoKeyboardTest {
    @Test
    void createKeyboard() {
        String gameName = "name";
        assertEquals(GameInfoKeyboard.createKeyboard(gameName).getClass(),
                InlineKeyboardMarkup.class);
    }
}
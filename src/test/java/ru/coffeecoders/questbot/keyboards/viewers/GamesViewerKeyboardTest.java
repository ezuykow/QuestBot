package ru.coffeecoders.questbot.keyboards.viewers;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.junit.jupiter.api.Test;
import ru.coffeecoders.questbot.entities.Game;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GamesViewerKeyboardTest {
    @Test
    void createKeyboardGamesCountMultipleMaxButtonCountTest() {
        assertEquals(GamesViewerKeyboard.createKeyboard(List.of(new Game(),new Game(),new Game(),new Game(),new Game(),
                        new Game(),new Game(),new Game())).getClass(), InlineKeyboardMarkup.class);
    }
    @Test
    void createKeyboardGamesCountNotMultipleMaxButtonCountTest() {
        assertEquals(GamesViewerKeyboard.createKeyboard(List.of(new Game())).getClass(), InlineKeyboardMarkup.class);
    }
}
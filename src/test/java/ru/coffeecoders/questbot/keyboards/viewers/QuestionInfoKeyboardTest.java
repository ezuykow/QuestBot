package ru.coffeecoders.questbot.keyboards.viewers;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionInfoKeyboardTest {
    @Test
    void createKeyboard() {
        int questionId = 111;
        assertEquals(QuestionInfoKeyboard.createKeyboard(questionId).getClass(), InlineKeyboardMarkup.class);
    }
}
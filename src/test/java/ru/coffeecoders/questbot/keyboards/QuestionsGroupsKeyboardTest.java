package ru.coffeecoders.questbot.keyboards;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.junit.jupiter.api.Test;
import ru.coffeecoders.questbot.entities.QuestionGroup;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionsGroupsKeyboardTest {
    @Test
    void createKeyboard() {
        String groupName = "name";
        QuestionGroup g = new QuestionGroup(groupName);
        assertEquals(QuestionsGroupsKeyboard.createKeyboard(List.of(g)).getClass(),
                InlineKeyboardMarkup.class);
    }
}
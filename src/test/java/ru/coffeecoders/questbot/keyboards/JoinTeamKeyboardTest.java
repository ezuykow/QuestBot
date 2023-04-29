package ru.coffeecoders.questbot.keyboards;

import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JoinTeamKeyboardTest {    @Test
    void createKeyboardTest() {
        String teamName1 = "teamName1";
        String teamName2 = "teamName2";
        List<String> teamsNames = List.of(teamName1, teamName2);
        assertEquals(JoinTeamKeyboard.createKeyboard(teamsNames).getClass(), ReplyKeyboardMarkup.class);
    }
}
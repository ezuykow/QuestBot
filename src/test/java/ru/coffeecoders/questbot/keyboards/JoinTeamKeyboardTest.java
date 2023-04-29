package ru.coffeecoders.questbot.keyboards;

import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class JoinTeamKeyboardTest {

    @Test
    void createKeyboardTest() {
        String teamName1 = "teamName1";
        String teamName2 = "teamName2";
        List<String> teamsNames = List.of(teamName1, teamName2);
        assertEquals(JoinTeamKeyboard.createKeyboard(teamsNames).getClass(), ReplyKeyboardMarkup.class);
    }
}
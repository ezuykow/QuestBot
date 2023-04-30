package ru.coffeecoders.questbot.managers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.entities.QuestionGroup;
import ru.coffeecoders.questbot.logs.LogSender;
import ru.coffeecoders.questbot.services.QuestionGroupService;
import ru.coffeecoders.questbot.services.QuestionService;
import ru.coffeecoders.questbot.services.TaskService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author ezuykow
 */
@ExtendWith(MockitoExtension.class)
class GameManagerTest {

    @Mock
    private QuestionService questionService;
    @Mock
    private QuestionGroupService questionGroupService;
    @Mock
    private TaskService taskService;
    @Mock
    private LogSender logger;

    @InjectMocks
    private GameManager gameManager;

    @Test
    public void shouldSaveTasksList() {
        final Game game = new Game("gameName",
                new int[]{1, 2},
                10,
                20,
                10,
                3,
                2,
                5);
        final QuestionGroup group = new QuestionGroup("group");
        final Question question = new Question();

        doNothing().when(logger).warn(anyString());
        when(questionGroupService.findById(anyLong())).thenReturn(Optional.of(group));
        when(questionService.findAll()).thenReturn(Collections.singletonList(question));
        doNothing().when(taskService).saveAll(any());

        gameManager.createTasks(1, game);
        verify(taskService).saveAll(any());
    }
}
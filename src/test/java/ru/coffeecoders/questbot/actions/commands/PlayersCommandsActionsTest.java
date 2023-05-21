package ru.coffeecoders.questbot.actions.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.response.SendResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.MessageToDelete;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.entities.Task;
import ru.coffeecoders.questbot.entities.Team;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.services.*;
import ru.coffeecoders.questbot.validators.GameValidator;
import ru.coffeecoders.questbot.viewers.TasksViewer;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayersCommandsActionsTest {
    @Mock
    private TeamService teamService;
    @Mock
    private TaskService taskService;
    @Mock
    private MessageSender msgSender;
    @Mock
    private QuestionService questionService;
    @Mock
    private GameValidator gameValidator;
    @Mock
    private GlobalChatService globalChatService;
    @Mock
    private MessageToDeleteService msgToDelService;
    @Mock
    private TasksViewer tasksViewer;

    @Mock
    private ExtendedUpdate exUpdate;
    @Mock
    private SendResponse response;
    @Mock
    private Message message;
    @Mock
    private Messages messages;
    @Mock
    private ReplyKeyboardMarkup keyboard;

    private final String teamName1 = "hkj";
    private final String teamName2 = "iop";
    private final String questionText = "ghj ghj";
    private final String answerFormat = "op[";
    private final String mapUrl = "125478";
    private final int teamScore1 = 123;
    private final int teamScore2 = 456;
    private final long id = 1L;

    @InjectMocks
    private PlayersCommandsActions actions;

    @BeforeEach
    void setUp() {
        when(exUpdate.getMessageChatId()).thenReturn(id);
    }

    @Test
    void showTasksIfGameNotStarted() {
        String msg = "Запущенных игр нет";
        when(gameValidator.isGameStarted(exUpdate.getMessageChatId())).thenReturn(false);
        when(messages.haventStartedGame()).thenReturn(msg);
        Mockito.verify(msgSender).send(id, msg);
    }

    @Test
    void regTeamIfGameCreated() {
        String relateTo = "REGTEAM";
        String msg = "В ответ на это сообщение введите название своей команды";
        int replyToMsgId = 12;
        int msgId = 11;
        long userId = 111L;
        when(exUpdate.getMessageFromUserId()).thenReturn(userId);
        when(exUpdate.getMessageId()).thenReturn(msgId);
        when(msgSender.sendForceReply(id, msg, msgId))
                .thenReturn(response);
        when(response.message()).thenReturn(message);
        when(message.messageId()).thenReturn(replyToMsgId);
        when(gameValidator.isGameStarted(exUpdate.getMessageChatId())).thenReturn(false);
        when(gameValidator.isGameCreating(exUpdate.getMessageChatId())).thenReturn(true);
        when(messages.enterTeamName()).thenReturn(msg);
        actions.regTeam(exUpdate);
        Mockito.verify(msgSender).sendForceReply(id, msg, msgId);
        Mockito.verify(msgToDelService).save(new MessageToDelete(msgId, userId, relateTo, id, true));
        Mockito.verify(msgToDelService).save(new MessageToDelete(replyToMsgId, userId, relateTo, id, true));
    }

    @Test
    void joinTeamIfTeamsNotExistTest() {
        String msg = "Пока не зарегистрировано ни одной команды";
        when(gameValidator.isGameStarted(exUpdate.getMessageChatId())).thenReturn(false);
        when(gameValidator.isGameCreating(exUpdate.getMessageChatId())).thenReturn(true);
        when(teamService.findAll()).thenReturn(List.of());
        when(messages.noTeamsRegisteredYet()).thenReturn(msg);
        actions.joinTeam(exUpdate);
        verify(msgSender).send(id, msg);
    }

    private Optional<Question> createQuestion(int i) {
        Question question = new Question();
        question.setQuestionId(i);
        question.setQuestion(questionText);
        question.setAnswerFormat(answerFormat);
        question.setAdditional(mapUrl);
        return Optional.of(question);
    }

    private Task createTask(String gameName, int questionId) {
        return new Task(gameName, questionId, null, id, 1);
    }

    private List<Team> createTeamsList() {
        return List.of(new Team(teamName1, null, teamScore1, id),
                new Team(teamName2, null, teamScore2, id));
    }

    private String appendTeamScore(String teamName, int teamScore) {
        return Character.toString(0x1F396) + " " + teamName + ": " + teamScore + ";\n";
    }

    private String appendQuestion() {
        return Character.toString(0x2753)
                + " Вопрос:" + questionText
                + "\nФормат ответа: " + answerFormat
                + "\n На карте: " + mapUrl + "\n";
    }
}
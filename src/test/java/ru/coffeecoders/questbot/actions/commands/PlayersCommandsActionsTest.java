package ru.coffeecoders.questbot.actions.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.response.SendResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.*;
import ru.coffeecoders.questbot.keyboards.JoinTeamKeyboard;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.services.*;
import ru.coffeecoders.questbot.validators.GameValidator;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

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
    private ExtendedUpdate exUpdate;
    @Mock
    private SendResponse response;
    @Mock
    private Message message;
    @Mock
    private Messages messages;
    @Mock
    private ReplyKeyboardMarkup keyboard;


    @InjectMocks
    private PlayersCommandsActions actions;

    private final String teamName1 = "hkj";
    private final String teamName2 = "iop";
    private final String questionText = "ghj ghj";
    private final String answerFormat = "op[";
    private final String mapUrl = "125478";
    private final int teamScore1 = 123;
    private final int teamScore2 = 456;
    private final long id = 1L;

    @BeforeEach
    void setUp() {
        when(exUpdate.getMessageChatId()).thenReturn(id);
    }

    @Test
    void showScores() {
        when(teamService.findAll()).thenReturn(createTeamsList());
        actions.showScores(exUpdate.getMessageChatId());
        Mockito.verify(msgSender).send(id,
                appendTeamScore(teamName1, teamScore1) +
                        appendTeamScore(teamName2, teamScore2)
        );
    }

    @Test
    void showTasksIfGameNotStarted() {
        when(gameValidator.isGameStarted(exUpdate.getMessageChatId())).thenReturn(false);
        when(messages.haventStartedGame()).thenReturn("Запущенных игр нет");
        actions.showTasks(exUpdate.getMessageChatId());
        Mockito.verify(msgSender).send(id, "Запущенных игр нет");
    }

    @Test
    void showTasksIfGameStarted() {
        String gameName = "ggg";
        int questionId1 = 1;
        int questionId2 = 2;
        when(gameValidator.isGameStarted(exUpdate.getMessageChatId())).thenReturn(true);
        when(globalChatService.findById(exUpdate.getMessageChatId())).thenReturn(Optional.of(
                new GlobalChat(id, gameName, true, null
                )));
        when(taskService.findByGameName(gameName)).thenReturn(List.of(
                createTask(gameName, questionId1), createTask(gameName, questionId2)
        ));
        when(questionService.findById(questionId1)).thenReturn(createQuestion(questionId1));
        when(questionService.findById(questionId2)).thenReturn(createQuestion(questionId2));
        actions.showTasks(exUpdate.getMessageChatId());
        Mockito.verify(msgSender).send(id, appendQuestion() + appendQuestion());
    }

    @Test
    void regTeamIfGameCreated() {
        String relateTo = "REGTEAM";
        int replyToMsgId = 12;
        int msgId = 11;
        long userId = 111L;
        when(exUpdate.getMessageFromUserId()).thenReturn(userId);
        when(exUpdate.getMessageId()).thenReturn(msgId);
        when(msgSender.send(id, "В ответ на это сообщение введите название своей команды", msgId))
                .thenReturn(response);
        when(response.message()).thenReturn(message);
        when(message.messageId()).thenReturn(replyToMsgId);
        when(gameValidator.isGameStarted(exUpdate.getMessageChatId())).thenReturn(false);
        when(gameValidator.isGameCreating(exUpdate.getMessageChatId())).thenReturn(true);
        when(messages.enterTeamName())
                .thenReturn("В ответ на это сообщение введите название своей команды");
        actions.regTeam(exUpdate);
        Mockito.verify(msgSender).send(id,
                "В ответ на это сообщение введите название своей команды", msgId);
        Mockito.verify(msgToDelService).save(new MessageToDelete(msgId, userId, relateTo, id, true));
        Mockito.verify(msgToDelService).save(new MessageToDelete(replyToMsgId, userId, relateTo, id, true));
    }

    @Test
    void joinTeamIfTeamsExistTest() {
        int msgId = 11;
        String userName = "user";
        when(exUpdate.getUsernameFromMessage()).thenReturn(userName);
        when(gameValidator.isGameStarted(exUpdate.getMessageChatId())).thenReturn(false);
        when(gameValidator.isGameCreating(exUpdate.getMessageChatId())).thenReturn(true);
        when(teamService.findAll()).thenReturn(createTeamsList());
        when(exUpdate.getMessageId()).thenReturn(msgId);
        when(messages.chooseYourTeam())
                .thenReturn(", выберите команду, в которую хотите вступить");
        try (MockedStatic<JoinTeamKeyboard> theMock = mockStatic(JoinTeamKeyboard.class)) {
            theMock.when(() -> JoinTeamKeyboard.createKeyboard(teamService.findAll()
                    .stream().map(Team::getTeamName).toList())).thenReturn(keyboard);
            actions.joinTeam(exUpdate);
            verify(msgSender).send(id, "@" + userName + ", выберите команду, в которую хотите вступить",
                    keyboard, msgId);
        }

    }

    @Test
    void joinTeamIfTeamsNotExistTest() {
        when(gameValidator.isGameStarted(exUpdate.getMessageChatId())).thenReturn(false);
        when(gameValidator.isGameCreating(exUpdate.getMessageChatId())).thenReturn(true);
        when(teamService.findAll()).thenReturn(List.of());
        when(messages.noTeamsRegisteredYet())
                .thenReturn("Пока не зарегистрировано ни одной команды");
        actions.joinTeam(exUpdate);
        verify(msgSender).send(id, "Пока не зарегистрировано ни одной команды");
    }

    private Optional<Question> createQuestion(int i) {
        Question question = new Question();
        question.setQuestionId(i);
        question.setQuestion(questionText);
        question.setAnswerFormat(answerFormat);
        question.setMapUrl(mapUrl);
        return Optional.of(question);
    }

    private Task createTask(String gameName, int questionId) {
        return new Task(gameName, questionId, null, id);
    }

    private List<Team> createTeamsList() {
        return List.of(new Team(teamName1, null, teamScore1, id ),
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
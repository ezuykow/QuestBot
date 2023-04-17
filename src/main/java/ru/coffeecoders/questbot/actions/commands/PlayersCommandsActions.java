package ru.coffeecoders.questbot.actions.commands;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.entities.Task;
import ru.coffeecoders.questbot.entities.Team;
import ru.coffeecoders.questbot.keyboards.JoinTeamKeyboard;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.services.GameService;
import ru.coffeecoders.questbot.services.QuestionService;
import ru.coffeecoders.questbot.services.TaskService;
import ru.coffeecoders.questbot.services.TeamService;

import java.util.*;

/**
 * @author anna
 */
@Component
public class PlayersCommandsActions {
    private final TeamService teamService;
    private final TaskService taskService;
    private final GameService gameService;
    private final MessageSender msgSender;
    private final QuestionService questionService;
    private final Environment env;

    public PlayersCommandsActions(TeamService teamService,
                                  TaskService taskService,
                                  GameService gameService,
                                  MessageSender msgSender,
                                  QuestionService questionService,
                                  Environment env) {
        this.teamService = teamService;
        this.taskService = taskService;
        this.gameService = gameService;
        this.msgSender = msgSender;
        this.questionService = questionService;
        this.env = env;
    }

    /**
     * Собирает сообщение из пар название команды-счет и передает его
     * в {@link MessageSender}
     * @param chatId id чата
     */
    public void showScores(long chatId) {
        StringBuilder sb = new StringBuilder();
        teamService.findAll().forEach(t -> sb.append(Character.toString(0x1F396))
                .append(" ")
                .append(t.getTeamName())
                .append(": ")
                .append(t.getScore())
                .append(";\n"));
        msgSender.send(chatId, sb.toString());
    }

    /**
     * Получает название текущей игры по chatId, передает в {@link MessageSender} список
     * актуальных вопросов
     * @param chatId id чата
     */
    public void showTasks(long chatId) {
        final Optional<Game> game = gameService.findByChatId(chatId);
        if (game.isEmpty()) {
            msgSender.send(chatId, env.getProperty("messages.players.haventStartedGame"));
        } else {
            msgSender.send(chatId, getActualTasks(game.get().getGameName()));
        }
    }

    /**
     * Отправляет в {@link  MessageSender} строку с предложением ввести название команды
     *
     * @param update апдейт
     */
    public void regTeam(ExtendedUpdate update) {
        if (gameCreated() && !gameStarted()) {
            msgSender.send(update.getMessageChatId(), env.getProperty("messages.players.enterTeamName"),
                    update.getMessageId());
        }
    }

    /**
     * Получает названия всех команд (Проверяет, что хотябы одна команда существует), и передает в {@link MessageSender}
     * клавиатуру с этими названиями
     *
     //* @param chatId id чата
     */
    public void joinTeam(ExtendedUpdate update) {
        List<String> teamsNames = teamService.findAll().stream().map(Team::getTeamName).toList();
        if (teamsNames.isEmpty()) {
            msgSender.send(update.getMessageChatId(), env.getProperty("messages.players.noTeamsRegisteredYet"));
        } else {
            msgSender.send(update.getMessageChatId(),
                    "@" + update.getUsernameFromMessage() + env.getProperty("messages.players.chooseYourTeam"),
                    JoinTeamKeyboard.createKeyboard(teamsNames), update.getMessageId());
        }
    }

    private String getActualTasks(String gameName) {
        StringBuilder sb = new StringBuilder();
        taskService.findByGameName(gameName)
                .stream().filter(t -> t.getPerformedTeamName() == null).map(Task::getQuestionId).toList()
                .forEach(id -> questionService.findById(id).ifPresent(
                        q -> sb.append(Character.toString(0x2753))
                                .append(" Вопрос:")
                                .append(q.getQuestion())
                                .append("\nФормат ответа: ")
                                .append(q.getAnswerFormat())
                                .append("\n На карте: ")
                                .append(q.getMapUrl())
                                .append("\n")));
        return sb.toString();
    }
}

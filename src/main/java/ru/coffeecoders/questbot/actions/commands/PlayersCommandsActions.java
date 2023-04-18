package ru.coffeecoders.questbot.actions.commands;

import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.*;
import ru.coffeecoders.questbot.keyboards.JoinTeamKeyboard;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.repositories.MessageToDeleteRepository;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.services.*;

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
    private final MessageToDeleteService messageToDeleteService;
    private final Environment env;

    public PlayersCommandsActions(TeamService teamService,
                                  TaskService taskService,
                                  GameService gameService,
                                  MessageSender msgSender,
                                  QuestionService questionService,
                                  MessageToDeleteService messageToDeleteService, Environment env, MessageToDeleteRepository mtdRepository) {
        this.teamService = teamService;
        this.taskService = taskService;
        this.gameService = gameService;
        this.msgSender = msgSender;
        this.questionService = questionService;
        this.messageToDeleteService = messageToDeleteService;
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
                .append(";\n")
        );
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
        if (gameCreated(update) && !gameStarted(update)) {
            SendResponse response =
                    msgSender.send(update.getMessageChatId(), env.getProperty("messages.players.enterTeamName"),
                            update.getMessageId()
                    );
            saveToMessageToDelete(update.getMessageId(), update, "REGTEAM", true);
            saveToMessageToDelete(response.message().messageId(), update, "REGTEAM", true);
        }
    }

    /**
     * Получает названия всех команд (Проверяет, что хотябы одна команда существует), и передает в {@link MessageSender}
     * клавиатуру с этими названиями
     *
     //* @param chatId id чата
     */
    public void joinTeam(ExtendedUpdate update) {
        if (gameCreated(update) && !gameStarted(update)) {
            List<String> teamsNames = teamService.findAll()
                    .stream().map(Team::getTeamName).toList();
            if (teamsNames.isEmpty()) {
                msgSender.send(update.getMessageChatId(), env.getProperty("messages.players.noTeamsRegisteredYet"));
            } else {
                msgSender.send(update.getMessageChatId(),
                        "@" + update.getUsernameFromMessage() + env.getProperty("messages.players.chooseYourTeam"),
                        JoinTeamKeyboard.createKeyboard(teamsNames), update.getMessageId()
                );
            }
        }
    }

    private String getActualTasks(String gameName) {
        StringBuilder sb = new StringBuilder();
        taskService.findByGameName(gameName)
                .stream()
                .filter(t -> t.getPerformedTeamName() == null)
                .map(Task::getQuestionId).toList()
                .forEach(id -> questionService.findById(id).ifPresent(
                                q -> sb.append(createQuestionInfoMsg(q))
                        )
                );
        return sb.toString();
    }

    private boolean gameCreated(ExtendedUpdate update) {
        return gameService.findByChatId(update.getMessageChatId()).isPresent();
    }

    private boolean gameStarted(ExtendedUpdate update) {
        Optional<Game> game = gameService.findByChatId(update.getMessageChatId());
        return game.map(Game::isStarted).orElse(false);
    }

    private void saveToMessageToDelete(int msgId, ExtendedUpdate update, String relateTo, boolean active) {
        messageToDeleteService.save(
                new MessageToDelete(
                        msgId,
                        update.getMessageFromUserId(),
                        relateTo,
                        update.getMessageChatId(),
                        active
                )
        );
    }

    private String createQuestionInfoMsg(Question q) {
        return Character.toString(0x2753) +
                " Вопрос:" + q.getQuestion() +
                "\nФормат ответа: " + q.getAnswerFormat() +
                "\n На карте: " + q.getMapUrl() + "\n";
    }
}

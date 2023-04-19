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
import ru.coffeecoders.questbot.validators.GameValidator;

import java.util.*;

/**
 * @author anna
 */
@Component
public class PlayersCommandsActions {
    private final TeamService teamService;
    private final TaskService taskService;
    private final GlobalChatService globalChatService;
    private final MessageSender msgSender;
    private final QuestionService questionService;
    private final MessageToDeleteService messageToDeleteService;
    private final GameValidator gameValidator;
    private final Environment env;

    public PlayersCommandsActions(TeamService teamService, TaskService taskService,
                                  GlobalChatService globalChatService, MessageSender msgSender,
                                  QuestionService questionService, MessageToDeleteService messageToDeleteService,
                                  MessageToDeleteRepository mtdRepository, GameValidator gameValidator,
                                  Environment env)
    {
        this.teamService = teamService;
        this.taskService = taskService;
        this.globalChatService = globalChatService;
        this.msgSender = msgSender;
        this.questionService = questionService;
        this.messageToDeleteService = messageToDeleteService;
        this.env = env;
        this.gameValidator = gameValidator;
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
     * @author anna
     * <br>Redact: ezuykow
     */
    public void showTasks(long chatId) {
        if (gameValidator.isGameStarted(chatId)) {
            msgSender.send(chatId,
                    getActualTasks(globalChatService.findById(chatId).map(GlobalChat::getCreatingGameName).get())
            );
        } else {
            msgSender.send(chatId, env.getProperty("messages.players.haventStartedGame"));
        }
    }

    /**
     * Отправляет в {@link  MessageSender} строку с предложением ввести название команды
     *
     * @param update апдейт
     * @author anna
     * <br>Redact: ezuykow
     */
    public void regTeam(ExtendedUpdate update) {
        long chatId = update.getMessageChatId();
        if (gameValidator.isGameCreating(chatId) && !gameValidator.isGameStarted(chatId)) {
            SendResponse response =
                    msgSender.send(chatId, env.getProperty("messages.players.enterTeamName"),
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
     * @author anna
     * <br>Redact: ezuykow
     */
    public void joinTeam(ExtendedUpdate update) {
        long chatId = update.getMessageChatId();
        if (gameValidator.isGameCreating(chatId) && !gameValidator.isGameStarted(chatId)) {
            List<String> teamsNames = teamService.findAll()
                    .stream().map(Team::getTeamName).toList();
            if (teamsNames.isEmpty()) {
                msgSender.send(chatId, env.getProperty("messages.players.noTeamsRegisteredYet"));
            } else {
                msgSender.send(chatId,
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

package ru.coffeecoders.questbot.actions.commands;

import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.entities.MessageToDelete;
import ru.coffeecoders.questbot.entities.Team;
import ru.coffeecoders.questbot.exceptions.NonExistentChat;
import ru.coffeecoders.questbot.exceptions.NonExistentGame;
import ru.coffeecoders.questbot.keyboards.JoinTeamKeyboard;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.services.GameService;
import ru.coffeecoders.questbot.services.GlobalChatService;
import ru.coffeecoders.questbot.services.MessageToDeleteService;
import ru.coffeecoders.questbot.services.TeamService;
import ru.coffeecoders.questbot.validators.GameValidator;
import ru.coffeecoders.questbot.viewers.TasksViewer;

import java.util.List;

/**
 * @author anna
 */
@Component
public class PlayersCommandsActions {

    private final TeamService teamService;
    private final GameService gameService;
    private final GlobalChatService globalChatService;
    private final TasksViewer tasksViewer;
    private final MessageSender msgSender;
    private final MessageToDeleteService messageToDeleteService;
    private final GameValidator gameValidator;
    private final Messages messages;

    public PlayersCommandsActions(TeamService teamService, GameService gameService, GlobalChatService globalChatService,
                                  TasksViewer tasksViewer, MessageSender msgSender,
                                  MessageToDeleteService messageToDeleteService,
                                  GameValidator gameValidator, Messages messages)
    {
        this.teamService = teamService;
        this.gameService = gameService;
        this.globalChatService = globalChatService;
        this.tasksViewer = tasksViewer;
        this.msgSender = msgSender;
        this.messageToDeleteService = messageToDeleteService;
        this.gameValidator = gameValidator;
        this.messages = messages;
    }

    //-----------------API START-----------------

    /**
     * Собирает сообщение из пар название команды-счет и передает его
     * в {@link MessageSender}
     * @param chatId id чата
     */
    public void showInfo(long chatId) {
        if (gameValidator.isGameStarted(chatId)) {
            List<Team> teams = teamService.findByChatId(chatId);
            GlobalChat chat = globalChatService.findById(chatId).orElseThrow(NonExistentChat::new);
            Game game = gameService.findByName(chat.getCreatingGameName())
                    .orElseThrow(NonExistentGame::new);
            msgSender.send(chatId, createInfoText(game, chat, teams));
        }
    }

    /**
     * Получает название текущей игры по chatId, передает в {@link MessageSender} список
     * актуальных вопросов
     * @param chatId id чата
     * @author anna
     * @Redact: ezuykow
     */
    public void showQuestions(long chatId) {
        if (gameValidator.isGameStarted(chatId)) {
            tasksViewer.showActualTasks(chatId);
        } else {
            msgSender.send(chatId, messages.haventStartedGame());
        }
    }

    /**
     * Отправляет в {@link  MessageSender} строку с предложением ввести название команды
     *
     * @param update апдейт
     * @author anna
     * @Redact: ezuykow
     */
    public void regTeam(ExtendedUpdate update) {
        long chatId = update.getMessageChatId();
        if (gameValidator.isGameCreating(chatId) && !gameValidator.isGameStarted(chatId)) {
            SendResponse response =
                    msgSender.sendForceReply(chatId, messages.enterTeamName(),
                            update.getMessageId()
                    );
            saveToMessageToDelete(update.getMessageId(), update.getMessageFromUserId(), chatId);
            saveToMessageToDelete(response.message().messageId(), update.getMessageFromUserId(), chatId);
        }
    }

    /**
     * Получает названия всех команд (Проверяет, что хотябы одна команда существует), и передает в {@link MessageSender}
     * клавиатуру с этими названиями
     *
     //* @param chatId id чата
     * @author anna
     * @Redact: ezuykow
     */
    public void joinTeam(ExtendedUpdate update) {
        long chatId = update.getMessageChatId();
        if (gameValidator.isGameCreating(chatId) && !gameValidator.isGameStarted(chatId)) {
            List<String> teamsNames = teamService.findAll()
                    .stream().map(Team::getTeamName).toList();
            validateTeamListAndSentJoinTeamKeyboard(teamsNames, chatId, update);
        }
    }

    //-----------------API END-----------------

    /**
     * Создает новый {@link MessageToDelete} и вызывает для него {@link MessageToDeleteService#save}
     * @param msgId id сообщение, которое нужно будет удалить
     * @param userId id пользователя, который отправил сообщение
     * @param chatId id чата, в котором нужно будет удалить сообщение
     * @author ezuykow
     */
    private void saveToMessageToDelete(int msgId, long userId, long chatId) {
        messageToDeleteService.save(
                new MessageToDelete(msgId, userId, "REGTEAM", chatId, true));
    }

    /**
     * @author ezuykow
     */
    private String createInfoText(Game game, GlobalChat chat, List<Team> teams) {
        StringBuilder sb = new StringBuilder();
        sb.append("\uD83C\uDFB2Информация об игре\uD83C\uDFB2\n\n");
        sb.append("⏰ Оставшееся время - ").append(game.getMaxTimeMinutes() - chat.getMinutesSinceStart())
                .append(" минут\n\n");
        teams.forEach( t ->
                sb.append("\uD83D\uDC65 Команда \"").append(t.getTeamName()).append("\" - ")
                        .append(t.getScore()).append(" очков;\n")
        );
        return sb.toString();
    }

    /**
     * @author ezuykow
     */
    private void validateTeamListAndSentJoinTeamKeyboard(List<String> teamsNames, long chatId, ExtendedUpdate update) {
        if (teamsNames.isEmpty()) {
            msgSender.send(chatId, messages.noTeamsRegisteredYet());
        } else {
            msgSender.send(chatId,
                    "@" + update.getUsernameFromMessage() + messages.chooseYourTeam(),
                    JoinTeamKeyboard.createKeyboard(teamsNames), update.getMessageId()
            );
        }
    }
}

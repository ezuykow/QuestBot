package ru.coffeecoders.questbot.actions.commands;

import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.MessageToDelete;
import ru.coffeecoders.questbot.entities.Team;
import ru.coffeecoders.questbot.keyboards.JoinTeamKeyboard;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.services.MessageToDeleteService;
import ru.coffeecoders.questbot.services.TeamService;
import ru.coffeecoders.questbot.validators.GameValidator;

import java.util.List;

/**
 * @author anna
 */
@Component
public class PlayersCommandsActions {

    private final TeamService teamService;
    private final MessageSender msgSender;
    private final MessageToDeleteService messageToDeleteService;
    private final GameValidator gameValidator;
    private final Messages messages;

    public PlayersCommandsActions(TeamService teamService, MessageSender msgSender,
                                  MessageToDeleteService messageToDeleteService,
                                  GameValidator gameValidator, Messages messages)
    {
        this.teamService = teamService;
        this.msgSender = msgSender;
        this.messageToDeleteService = messageToDeleteService;
        this.gameValidator = gameValidator;
        this.messages = messages;
    }

    //-----------------API START-----------------

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
    private void validateTeamListAndSentJoinTeamKeyboard(List<String> teamsNames, long chatId, ExtendedUpdate update) {
        if (teamsNames.isEmpty()) {
            msgSender.send(chatId, messages.noTeamsRegisteredYet());
        } else {
            SendResponse response = msgSender.send(chatId,
                    "@" + update.getUsernameFromMessage() + messages.chooseYourTeam(),
                    JoinTeamKeyboard.createKeyboard(teamsNames), update.getMessageId()
            );
            saveToMessageToDelete(update.getMessageId(), update.getMessageFromUserId(), chatId);
            saveToMessageToDelete(response.message().messageId(), update.getMessageFromUserId(), chatId);
        }
    }
}

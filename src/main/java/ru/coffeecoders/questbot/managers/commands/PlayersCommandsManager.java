package ru.coffeecoders.questbot.managers.commands;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.commands.Commands;
import ru.coffeecoders.questbot.commands.actions.PlayersCommandsActions;
import ru.coffeecoders.questbot.models.ExtendedUpdate;

/**
 * @author anna
 */
@Component
public class PlayersCommandsManager {
    private final PlayersCommandsActions playersCommandsActions;

    public PlayersCommandsManager(PlayersCommandsActions playersCommandsActions) {
        this.playersCommandsActions = playersCommandsActions;
    }

    /**
     * Вызывает определенный метод {@link PlayersCommandsActions} в зависимости от того,
     * какая команда была передана в параметрах
     * @param update - апдейт
     * @param cmd - команда (enum)
     */
    public void manageCommand(ExtendedUpdate update, Commands.Command cmd) {
        long chatId = update.getMessageChatId();
        switch (cmd) {
            case SCORE -> playersCommandsActions.showScores(chatId);
            case TASKS -> playersCommandsActions.showTasks(chatId);
            case REGTEAM -> playersCommandsActions.regTeam(chatId);
            case JOINTEAM -> playersCommandsActions.joinTeam(update);
        }
    }
}
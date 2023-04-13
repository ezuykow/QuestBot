package ru.coffeecoders.questbot.managers.commands;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;

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
    public void manageCommand(Update update, Commands.Command cmd) {
        long chatId = update.message().chat().id();
        switch (cmd) {
            case SCORE -> playersCommandsActions.showScores(chatId);
            case TASKS -> playersCommandsActions.showTasks(chatId);
            case REGTEAM -> playersCommandsActions.regTeam(chatId);
            case JOINTEAM -> playersCommandsActions.joinTeam(chatId);
        }
    }
}
package ru.coffeecoders.questbot.commands.players;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.commands.players.PlayersCommandsActions;

/**
 * @author anna
 */
@Component
public class PlayersCommandsManager {
    private final PlayersCommandsActions playersCommandsActions;

    public PlayersCommandsManager(PlayersCommandsActions playersCommandsActions) {
        this.playersCommandsActions = playersCommandsActions;
    }

    public void manageCommand(Update update, Commands.Command cmd) {
        long chatId = update.message().chat().id();
        switch (cmd) {
            case SCORE -> playersCommandsActions.showScore(chatId);
            case TASKS -> playersCommandsActions.showTasks();
            case REGTEAM -> playersCommandsActions.regTeam();
            case JOINTEAM -> playersCommandsActions.joinTeam(chatId);
        }
    }
}

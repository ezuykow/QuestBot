package ru.coffeecoders.questbot.actions;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Team;
import ru.coffeecoders.questbot.managers.commands.Command;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.services.GameService;

/**
 * @author ezuykow
 */
@Component
public class SimpleMessageActions {

    private final GameService gameService;

    public SimpleMessageActions(GameService gameService) {
        this.gameService = gameService;
    }

    public void performReplyToCommand(ExtendedUpdate update, Command cmd) {
        switch (cmd) {
            case REGTEAM -> registerNewTeam(update);
            case JOINTEAM -> {}
        }
    }

    private void registerNewTeam(ExtendedUpdate update) {
        if (update.hasMessage()) {
            Team newTeam = new Team();
            newTeam.setTeamName(update.getMessageText());
            newTeam.setGameName(gameService.findByChatId(update.getMessageChatId()).or);
            newTeam.setScore(0);
        }

    }
}

package ru.coffeecoders.questbot.actions;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Player;
import ru.coffeecoders.questbot.entities.Team;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.services.GameService;
import ru.coffeecoders.questbot.services.PlayerService;
import ru.coffeecoders.questbot.services.TeamService;

/**
 * @author ezuykow
 */
@Component
public class SimpleMessageActions {

    private final GameService gameService;
    private final TeamService teamService;
    private final PlayerService playerService;
    private final MessageSender msgSender;

    public SimpleMessageActions(GameService gameService, TeamService teamService, PlayerService playerService, MessageSender msgSender) {
        this.gameService = gameService;
        this.teamService = teamService;
        this.playerService = playerService;
        this.msgSender = msgSender;
    }

    public void registerNewTeam(ExtendedUpdate update) {
        if (update.hasMessageText()) {
            Team newTeam = new Team();
            newTeam.setTeamName(update.getMessageText());
            newTeam.setGameName(gameService.findByChatId(update.getMessageChatId()).get().getGameName());
            newTeam.setScore(0);
            checkTeamForExistAndSave(update, newTeam);
        }
    }

    private void checkTeamForExistAndSave(ExtendedUpdate update, Team newTeam) {
        teamService.findByTeamName(newTeam.getTeamName())
                .ifPresentOrElse(
                        team -> msgSender.send(update.getMessageChatId(),
                                "Команда \"" + team.getTeamName() + "\" уже существует!"),
                        () -> {
                            teamService.save(newTeam);
                            msgSender.send(update.getMessageChatId(),
                                    "@" + update.getUsernameFromMessage() +
                                            " создал команду \"" + newTeam.getTeamName() + "\"");
                            addPlayersWithTeam(
                                    update.getMessageFromUserId(),
                                    gameService.findByChatId(update.getMessageChatId()).get().getGameName(),
                                    newTeam.getTeamName(),
                                    update
                            );
                        }
                );
    }

    public void joinTeam(ExtendedUpdate update) {
        addPlayersWithTeam(
                update.getMessageFromUserId(),
                gameService.findByChatId(update.getMessageChatId()).get().getGameName(),
                update.getMessageText(),
                update
        );
    }

    private void addPlayersWithTeam(long tgUserId, String gameName, String teamName, ExtendedUpdate update) {
        Player newPlayer = new Player();
        newPlayer.setTgUserId(tgUserId);
        newPlayer.setGameName(gameName);
        newPlayer.setTeamName(teamName);
        playerService.save(newPlayer);
        msgSender.send(update.getMessageChatId(),
                "Игрок @" + update.getUsernameFromMessage() + " вступил в команду \"" +
                        update.getMessageText() + "\"");
    }
}

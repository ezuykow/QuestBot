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
                        () -> saveNewTeam(newTeam, update)
                );
    }

    public void joinTeam(ExtendedUpdate update, String teamName) {
        Player newPlayer = createNewPlayer(update.getMessageFromUserId(),
                gameService.findByChatId(update.getMessageChatId()).get().getGameName(),
                teamName);
        addPlayersWithTeam(newPlayer, update);
    }

    private void addPlayersWithTeam(Player player, ExtendedUpdate update) {
        playerService.save(player);
        msgSender.send(update.getMessageChatId(),
                "Игрок @" + update.getUsernameFromMessage() + " вступил в команду \"" +
                        update.getMessageText() + "\"");
    }

    private Player createNewPlayer(long tgUserId, String gameName, String teamName) {
        Player newPlayer = new Player();
        newPlayer.setTgUserId(tgUserId);
        newPlayer.setGameName(gameName);
        newPlayer.setTeamName(teamName);
        return newPlayer;
    }

    private void saveNewTeam(Team newTeam, ExtendedUpdate update) {
        teamService.save(newTeam);
        msgSender.send(update.getMessageChatId(),
                "@" + update.getUsernameFromMessage() +
                        " создал команду \"" + newTeam.getTeamName() + "\"");
        joinTeam(update, newTeam.getTeamName());
    }
}

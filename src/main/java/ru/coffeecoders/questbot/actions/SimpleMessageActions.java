package ru.coffeecoders.questbot.actions;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.MessageToDelete;
import ru.coffeecoders.questbot.entities.Player;
import ru.coffeecoders.questbot.entities.Team;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.repositories.MessageToDeleteRepository;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.services.GameService;
import ru.coffeecoders.questbot.services.MessageToDeleteService;
import ru.coffeecoders.questbot.services.PlayerService;
import ru.coffeecoders.questbot.services.TeamService;

import java.util.List;

/**
 * @author ezuykow
 */
@Component
public class SimpleMessageActions {

    private final GameService gameService;
    private final TeamService teamService;
    private final PlayerService playerService;
    private final MessageSender msgSender;
    private final MessageToDeleteService messageToDeleteService;

    public SimpleMessageActions(GameService gameService, TeamService teamService, PlayerService playerService, MessageSender msgSender, MessageToDeleteRepository mtdRepository, MessageToDeleteService messageToDeleteService) {
        this.gameService = gameService;
        this.teamService = teamService;
        this.playerService = playerService;
        this.msgSender = msgSender;
        this.messageToDeleteService = messageToDeleteService;
    }

    /**
     * Принимает апдейт, в тексте которога - название создаваемой команды.
     * Если такой команды еще не существует, то создает новую команду и записывает
     * создавшего игрока в нее
     * @param update апдейт с именем новой команды в тексте
     */
    public void registerNewTeam(ExtendedUpdate update) {
        if (update.hasMessageText()) {
            Team newTeam = new Team(
                    update.getMessageText(),
                    gameService.findByChatId(update.getMessageChatId()).get().getGameName(),
                    0
            );
            checkTeamForExistAndSave(update, newTeam);
        }
        saveToMessageToDelete(update, "REGTEAM", false);
        markUselessRegTeamMsgAsInactive(update);
    }

    /**
     * Записывает игрока по userId из апдейта в команду с именем {@code teamName}
     * @param update апдейт с id игрока
     * @param teamName имя выбранной команды
     */
    public void joinTeam(ExtendedUpdate update, String teamName) {
        Player newPlayer = createNewPlayer(update.getMessageFromUserId(),
                gameService.findByChatId(update.getMessageChatId()).get().getGameName(),
                teamName
        );
        addPlayersWithTeam(newPlayer, update);
    }

    private void checkTeamForExistAndSave(ExtendedUpdate update, Team newTeam) {
        teamService.findByTeamName(newTeam.getTeamName())
                .ifPresentOrElse(
                        team -> msgSender.send(update.getMessageChatId(),
                                "Команда \"" + team.getTeamName() + "\" уже существует!"),
                        () -> saveNewTeam(newTeam, update)
                );
    }

    private void addPlayersWithTeam(Player player, ExtendedUpdate update) {
        playerService.save(player);
        msgSender.send(update.getMessageChatId(),
                "Игрок @" + update.getUsernameFromMessage() + " вступил в команду \"" +
                        update.getMessageText() + "\""
        );
    }

    private Player createNewPlayer(long tgUserId, String gameName, String teamName) {
        return new Player(tgUserId, gameName, teamName);
    }

    private void saveNewTeam(Team newTeam, ExtendedUpdate update) {
        teamService.save(newTeam);
        msgSender.send(update.getMessageChatId(),
                "@" + update.getUsernameFromMessage() +
                        " создал команду \"" + newTeam.getTeamName() + "\"");
        joinTeam(update, newTeam.getTeamName());
    }

    private void saveToMessageToDelete(ExtendedUpdate update, String relateTo, boolean active) {
        messageToDeleteService.save(
                new MessageToDelete(
                        update.getMessageId(),
                        update.getMessageFromUserId(),
                        relateTo,
                        update.getMessageChatId(),
                        active
                )
        );
    }

    private void markUselessRegTeamMsgAsInactive(ExtendedUpdate update) {
        List<MessageToDelete> mtds = messageToDeleteService.findByUserId(update.getMessageFromUserId())
                .stream()
                .filter(m -> m.getChatId() == update.getMessageChatId())
                .filter(m -> m.getRelate_to().equals("REGTEAM"))
                .toList();
        mtds.forEach(m -> m.setActive(false));
        messageToDeleteService.saveAll(mtds);
    }
}

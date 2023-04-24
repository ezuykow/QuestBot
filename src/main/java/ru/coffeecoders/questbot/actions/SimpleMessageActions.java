package ru.coffeecoders.questbot.actions;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.MessageToDelete;
import ru.coffeecoders.questbot.entities.Player;
import ru.coffeecoders.questbot.entities.Team;
import ru.coffeecoders.questbot.exceptions.NonExistentChat;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.services.GlobalChatService;
import ru.coffeecoders.questbot.services.MessageToDeleteService;
import ru.coffeecoders.questbot.services.PlayerService;
import ru.coffeecoders.questbot.services.TeamService;

import java.util.List;

/**
 * @author ezuykow
 */
@Component
public class SimpleMessageActions {

    private final TeamService teamService;
    private final PlayerService playerService;
    private final GlobalChatService globalChatService;
    private final MessageSender msgSender;
    private final MessageToDeleteService messageToDeleteService;

    public SimpleMessageActions(TeamService teamService, PlayerService playerService,
                                GlobalChatService globalChatService, MessageSender msgSender,
                                MessageToDeleteService messageToDeleteService) {
        this.teamService = teamService;
        this.playerService = playerService;
        this.globalChatService = globalChatService;
        this.msgSender = msgSender;
        this.messageToDeleteService = messageToDeleteService;
    }

    //-----------------API START-----------------

    /**
     * Принимает апдейт, в тексте которого - название создаваемой команды.
     * Если такой команды еще не существует, то создает новую команду и записывает
     * создавшего игрока в нее
     * @param update апдейт с именем новой команды в тексте
     * @author ezuykow
     */
    public void registerNewTeam(ExtendedUpdate update) {
        if (update.hasMessageText()) {
            Team newTeam = new Team(
                    update.getMessageText(),
                    globalChatService.findById(update.getMessageChatId())
                            .orElseThrow(NonExistentChat::new).getCreatingGameName()
            );
            checkTeamForExistAndSave(update, newTeam);
        }
        saveToMessageToDelete(update);
        markUselessRegTeamMsgAsInactive(update);
    }

    /**
     * Записывает игрока по userId из апдейта в команду с именем {@code teamName}
     * @param update апдейт с id игрока
     * @param teamName имя выбранной команды
     * @author ezuykow
     */
    public void joinTeam(ExtendedUpdate update, String teamName) {
        Player newPlayer = createNewPlayer(update.getMessageFromUserId(),
                globalChatService.findById(update.getMessageChatId())
                        .orElseThrow(NonExistentChat::new).getCreatingGameName(),
                teamName
        );
        addPlayersWithTeam(newPlayer, update);
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void checkTeamForExistAndSave(ExtendedUpdate update, Team newTeam) {
        teamService.findByTeamName(newTeam.getTeamName())
                .ifPresentOrElse(
                        team -> msgSender.send(update.getMessageChatId(),
                                "Команда \"" + team.getTeamName() + "\" уже существует!"),
                        () -> saveNewTeam(newTeam, update)
                );
    }

    /**
     * @author ezuykow
     */
    private void addPlayersWithTeam(Player player, ExtendedUpdate update) {
        playerService.save(player);
        msgSender.send(update.getMessageChatId(),
                "Игрок @" + update.getUsernameFromMessage() + " вступил в команду \"" +
                        update.getMessageText() + "\""
        );
    }

    /**
     * @author ezuykow
     */
    private Player createNewPlayer(long tgUserId, String gameName, String teamName) {
        return new Player(tgUserId, gameName, teamName);
    }

    /**
     * @author ezuykow
     */
    private void saveNewTeam(Team newTeam, ExtendedUpdate update) {
        teamService.save(newTeam);
        msgSender.send(update.getMessageChatId(),
                "@" + update.getUsernameFromMessage() +
                        " создал команду \"" + newTeam.getTeamName() + "\"");
        joinTeam(update, newTeam.getTeamName());
    }

    /**
     * @author ezuykow
     */
    private void saveToMessageToDelete(ExtendedUpdate update) {
        messageToDeleteService.save(
                new MessageToDelete(
                        update.getMessageId(),
                        update.getMessageFromUserId(),
                        "REGTEAM",
                        update.getMessageChatId(),
                        false
                )
        );
    }

    /**
     * @author ezuykow
     */
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

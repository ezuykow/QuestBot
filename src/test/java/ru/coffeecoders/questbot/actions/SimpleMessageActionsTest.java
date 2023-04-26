package ru.coffeecoders.questbot.actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.entities.MessageToDelete;
import ru.coffeecoders.questbot.entities.Player;
import ru.coffeecoders.questbot.entities.Team;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.services.GlobalChatService;
import ru.coffeecoders.questbot.services.MessageToDeleteService;
import ru.coffeecoders.questbot.services.PlayerService;
import ru.coffeecoders.questbot.services.TeamService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleMessageActionsTest {
    @Mock
    private TeamService teamService;
    @Mock
    private PlayerService playerService;
    @Mock
    private GlobalChatService globalChatService;
    @Mock
    private MessageSender msgSender;
    @Mock
    private MessageToDeleteService messageToDeleteService;
    @Mock
    private ExtendedUpdate update;
    @Mock
    private GlobalChat chat;

    private final long chatId = 1L;
    private final long userId = 111L;
    private final String gameName = "gameName";
    private final String teamName = "teamName";
    private final String userName = "userName";

    @InjectMocks
    private SimpleMessageActions actions;

    @BeforeEach
    void setUp() {
        when(update.getMessageChatId()).thenReturn(chatId);
        when(update.getMessageText()).thenReturn(teamName);
        when(globalChatService.findById(chatId)).thenReturn(Optional.of(chat));
        when(chat.getCreatingGameName()).thenReturn(gameName);
    }

    @Test
    void registerNewTeamIfNotExistTest() {
        Team team = new Team(teamName, gameName, 0, chatId);
        MessageToDelete mtd = new MessageToDelete(12, userId, "REGTEAM", chatId, false);
        when(update.hasMessageText()).thenReturn(true);
        when(update.getMessageId()).thenReturn(12);
        when(update.getMessageFromUserId()).thenReturn(userId);
        when(update.getUsernameFromMessage()).thenReturn(userName);
        when(teamService.findByTeamName(teamName)).thenReturn(Optional.empty());
        when(messageToDeleteService.findByUserId(update.getMessageFromUserId())).thenReturn(List.of(mtd));
        actions.registerNewTeam(update);
        verify(teamService).save(team);
        verify(messageToDeleteService).save(mtd);
        verify(messageToDeleteService).saveAll(List.of(mtd));
        verify(msgSender).send(chatId, "@" + userName + " создал команду \"" + teamName + "\"");
    }

    @Test
    void registerNewTeamIfAlreadyExistTest() {
        Team team = new Team(teamName, gameName, 0, chatId);
        when(update.hasMessageText()).thenReturn(true);
        when(teamService.findByTeamName(teamName)).thenReturn(Optional.of(team));
        actions.registerNewTeam(update);
        verify(msgSender).send(update.getMessageChatId(), "Команда \"" + teamName + "\" уже существует!");
    }

    @Test
    void joinTeam() {
        when(update.getMessageFromUserId()).thenReturn(userId);
        when(update.getUsernameFromMessage()).thenReturn(userName);
        actions.joinTeam(update, teamName);
        verify(playerService).save(new Player(userId, gameName, teamName, chatId));
        verify(msgSender).send(chatId, "Игрок @" + userName + " вступил в команду \"" + teamName + "\"");
    }
}
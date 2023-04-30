package ru.coffeecoders.questbot.actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.entities.Team;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.services.GlobalChatService;
import ru.coffeecoders.questbot.services.MessageToDeleteService;
import ru.coffeecoders.questbot.services.PlayerService;
import ru.coffeecoders.questbot.services.TeamService;

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
    void registerNewTeamIfAlreadyExistTest() {
        Team team = new Team(teamName, gameName, 0, chatId);
        when(update.hasMessageText()).thenReturn(true);
        when(teamService.findByTeamName(teamName)).thenReturn(Optional.of(team));
        actions.registerNewTeam(update);
        verify(msgSender).send(update.getMessageChatId(), "Команда \"" + teamName + "\" уже существует!");
    }
}
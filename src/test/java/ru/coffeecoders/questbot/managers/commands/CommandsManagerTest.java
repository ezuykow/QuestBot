package ru.coffeecoders.questbot.managers.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import ru.coffeecoders.questbot.managers.AdminsCommandsManager;
import ru.coffeecoders.questbot.managers.CommandsManager;
import ru.coffeecoders.questbot.managers.PlayersCommandsManager;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.validators.ChatAndUserIdValidator;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandsManagerTest {
    @Mock
    private AdminsCommandsManager adminsCommandsManager;
    @Mock
    private PlayersCommandsManager playersCommandsManager;
    @Mock
    private ChatAndUserIdValidator validator;
    @Mock
    private MessageSender msgSender;
    @Mock
    private ExtendedUpdate exUpdate;
    @Mock
    private Environment env;

    @BeforeEach
    void setUp() {

    }

    @InjectMocks
    private CommandsManager commandsManager;


    @Test
    void manageInvalidCommand() {
        String cmd = "/skkk";
        when(exUpdate.getMessageChatId()).thenReturn(1L);
        when(exUpdate.getMessageText()).thenReturn(cmd);
        when(env.getProperty("messages.admins.invalidMsg")).thenReturn("Введена неверная команда");
        commandsManager.manageCommand(exUpdate);
        Mockito.verify(msgSender).send(exUpdate.getMessageChatId(), env.getProperty("messages.admins.invalidMsg"));

    }

    @Test
    void manageGlobalAdminCommand() {
        String cmd = "/start";
        when(exUpdate.getMessageFromUserId()).thenReturn(1L);
        when(exUpdate.getMessageText()).thenReturn(cmd);
        when(validator.isAdmin(exUpdate.getMessageFromUserId())).thenReturn(true);
        commandsManager.manageCommand(exUpdate);
        Mockito.verify(adminsCommandsManager).manageCommand(exUpdate,
                Command.valueOf(cmd.substring(1).toUpperCase()));
    }

    @Test
    void manageGlobalAdminCommandFromNotAdmin() {
        String cmd = "/start";
        when(exUpdate.getMessageFromUserId()).thenReturn(1L);
        when(exUpdate.getMessageText()).thenReturn(cmd);
        when(validator.isAdmin(exUpdate.getMessageFromUserId())).thenReturn(false);
        when(env.getProperty("messages.admins.cmdSendByNotAdmin"))
                .thenReturn("Эту команду может использовать только админ бота");
        commandsManager.manageCommand(exUpdate);
        Mockito.verify(msgSender).send(exUpdate.getMessageChatId(),
                env.getProperty("messages.admins.cmdSendByNotAdmin"));
    }

    @Test
    void manageAdminCommand() {
        String cmd = "/showquestions";
        when(exUpdate.getMessageChatId()).thenReturn(12L);
        when(exUpdate.getMessageFromUserId()).thenReturn(1L);
        when(exUpdate.getMessageText()).thenReturn(cmd);
        when(validator.isAdmin(exUpdate.getMessageFromUserId())).thenReturn(true);
        when(validator.isAdminChat(exUpdate.getMessageChatId())).thenReturn(true);
        commandsManager.manageCommand(exUpdate);
        Mockito.verify(adminsCommandsManager).manageCommand(exUpdate,
                Command.valueOf(cmd.substring(1).toUpperCase()));
    }

    @Test
    void manageAdminCommandInNotAdminChat() {
        String cmd = "/showquestions";
        when(exUpdate.getMessageChatId()).thenReturn(12L);
        when(exUpdate.getMessageText()).thenReturn(cmd);
        when(validator.isAdminChat(exUpdate.getMessageChatId())).thenReturn(false);
        commandsManager.manageCommand(exUpdate);
        Mockito.verify(msgSender).send(exUpdate.getMessageChatId(),
                env.getProperty("adminCmdInGlobalChat"));
    }

    @Test
    void managePlayerCommand() {
        String cmd = "/score";
        when(exUpdate.getMessageChatId()).thenReturn(12L);
        when(exUpdate.getMessageText()).thenReturn(cmd);
        when(validator.isGlobalChat(exUpdate.getMessageChatId())).thenReturn(true);
        commandsManager.manageCommand(exUpdate);
        Mockito.verify(playersCommandsManager).manageCommand(exUpdate,
                Command.valueOf(cmd.substring(1).toUpperCase()));
    }

    @Test
    void managePlayerCommandInNotGlobalChat() {
        String cmd = "/showquestions";
        when(exUpdate.getMessageChatId()).thenReturn(12L);
        when(exUpdate.getMessageText()).thenReturn(cmd);
        Mockito.lenient().when(validator.isGlobalChat(exUpdate.getMessageChatId())).thenReturn(true);
        commandsManager.manageCommand(exUpdate);
        Mockito.verify(msgSender).send(exUpdate.getMessageChatId(),
                env.getProperty("gameCmdInAdminChat"));
    }

}
package ru.coffeecoders.questbot.managers.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.validators.ChatAndUserValidator;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandsManagerTest {
    @Mock
    private AdminsCommandsManager adminsCommandsManager;
    @Mock
    private PlayersCommandsManager playersCommandsManager;
    @Mock
    private OwnerCommandsManager ownerCommandsManager;
    @Mock
    private ChatAndUserValidator validator;
    @Mock
    private MessageSender msgSender;
    @Mock
    private ExtendedUpdate exUpdate;
    @Mock
    private Messages messages;

    long chatId = 1L;
    long userId = 12L;

    @InjectMocks
    private CommandsManager commandsManager;

    @BeforeEach
    public void setUp() {
        when(exUpdate.getMessageChatId()).thenReturn(chatId);
    }

    @Test
    void manageInvalidCommand() {
        when(exUpdate.getMessageText()).thenReturn("/skkk");
        when(messages.invalidMsg()).thenReturn("Введена неверная команда");
        commandsManager.manageCommand(exUpdate);
        Mockito.verify(msgSender).send(exUpdate.getMessageChatId(), "Введена неверная команда");
    }

    @Test
    void manageGlobalAdminCommand() {
        String cmd = "/start";
        when(exUpdate.getMessageText()).thenReturn(cmd);
        when(exUpdate.getMessageFromUserId()).thenReturn(userId);
        when(validator.isOwner(exUpdate.getMessageFromUserId())).thenReturn(true);
        commandsManager.manageCommand(exUpdate);
        Mockito.verify(ownerCommandsManager).manageCommand(chatId,
                Command.valueOf(cmd.substring(1).toUpperCase()));
    }

    @Test
    void manageGlobalAdminCommandFromNotAdmin() {
        String cmd = "/start";
        when(exUpdate.getMessageText()).thenReturn(cmd);
        when(messages.isOwnerCommand()).thenReturn("Эту команду может использовать только владелец");
        commandsManager.manageCommand(exUpdate);
        Mockito.verify(msgSender).send(chatId, "Эту команду может использовать только владелец");
    }

    @Test
    void manageAdminCommand() {
        String cmd = "/showquestions";
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
        when(exUpdate.getMessageText()).thenReturn(cmd);
        when(validator.isAdminChat(exUpdate.getMessageChatId())).thenReturn(false);
        when(messages.adminCmdInGlobalChat())
                .thenReturn("Эту команду можно использовать только в администраторском чате");
        commandsManager.manageCommand(exUpdate);
        Mockito.verify(msgSender).send(chatId, "Эту команду можно использовать только в администраторском чате");
    }

    @Test
    void managePlayerCommand() {
        String cmd = "/score";
        when(exUpdate.getMessageText()).thenReturn(cmd);
        when(validator.isGlobalChat(exUpdate.getMessageChatId())).thenReturn(true);
        commandsManager.manageCommand(exUpdate);
        Mockito.verify(playersCommandsManager).manageCommand(exUpdate,
                Command.valueOf(cmd.substring(1).toUpperCase()));
    }

    @Test
    void managePlayerCommandInNotGlobalChat() {
        String cmd = "/showquestions";
        when(exUpdate.getMessageText()).thenReturn(cmd);
        Mockito.lenient().when(validator.isGlobalChat(exUpdate.getMessageChatId())).thenReturn(true);
        when(messages.adminCmdInGlobalChat()).thenReturn("В админском чате оставлена игровая команда");
        commandsManager.manageCommand(exUpdate);
        Mockito.verify(msgSender).send(chatId, "В админском чате оставлена игровая команда");
    }
}
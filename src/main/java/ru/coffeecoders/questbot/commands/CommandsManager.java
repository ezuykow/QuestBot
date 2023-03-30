package ru.coffeecoders.questbot.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//TODO поменять методы классов AdminsCommandsManager, PlayersCommandsManager, CommandsManagerMsgSender когда они будут готовы

@Component
public class CommandsManager {
    private final AdminsCommandsManager adminsCommandsManager;
    private final PlayersCommandsManager playersCommandsManager;
    private final Commands commands;
    private final AdminService adminService;
    private final AdminChatService adminChatService;
    private final GlobalChatService globalChatService;
    private final CommandsManagerMsgSender managerMsgSender;

    public CommandsManager(AdminsCommandsManager adminsCommandsManager, PlayersCommandsManager playersCommandsManager,
                           Commands commands, AdminService adminService, AdminChatService adminChatService,
                           GlobalChatService globalChatService, CommandsManagerMsgSender managerMsgSender) {
        this.adminsCommandsManager = adminsCommandsManager;
        this.playersCommandsManager = playersCommandsManager;
        this.commands = commands;
        this.adminService = adminService;
        this.adminChatService = adminChatService;
        this.globalChatService = globalChatService;
        this.managerMsgSender = managerMsgSender;
    }


    public void manageCommand(Update update) {
        String msg = update.message().text().trim();
        int delimiter = msg.indexOf(" ");
        String command;
        String other;
        if (delimiter == -1) {
            command = msg.substring(1).toUpperCase();
            other = "";
        } else {
            command = msg.substring(1, delimiter).toUpperCase();
            other = msg.substring(delimiter + 1);
        }

        if (commands.getAllGlobalAdminsCommands().contains(command)) {
            checkAndSendGlobalAdminsCommand(update, command, other);
        } else if (commands.getAllAdminsCommands().contains(command)) {
            checkAndSendAdminsCommand(update, command, other);
        } else if (commands.getAllPlayersCommands().contains(command)) {
            checkAndSendPlayersCommand(update, command, other);
        } else managerMsgSender.sendInvalidCommandMsg(update);
    }

    private void checkAndSendPlayersCommand(Update update, String command, String other) {
        if (globalChatService.findById(update.message().chat().id()).isPresent) {
            playersCommandsManager.doSome(update, command, other);
        } else managerMsgSender.sendNotGlobalChatMsg(update);
    }

    private void checkAndSendAdminsCommand(Update update, String command, String other) {
        if (adminChatService.findById(update.message().chat().id()).isPresent) {
            checkAndSendGlobalAdminsCommand(update, command, other);
        } else managerMsgSender.sendNotAdminChatMsg(update);
    }

    private void checkAndSendGlobalAdminsCommand(Update update, String command, String other) {
        if (adminService.findById(update.message().from().id()).isPresent) {
            adminsCommandsManager.doSome(update, command, other);
        } else managerMsgSender.sendNotAdminMsg(update);
    }
}

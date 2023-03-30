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
    private final AdminService adminService;
    private final AdminChatService adminChatService;
    private final GlobalChatService globalChatService;
    private final CommandsManagerMsgSender managerMsgSender;

    public CommandsManager(AdminsCommandsManager adminsCommandsManager, PlayersCommandsManager playersCommandsManager,
                           AdminService adminService, AdminChatService adminChatService,
                           GlobalChatService globalChatService, CommandsManagerMsgSender managerMsgSender) {
        this.adminsCommandsManager = adminsCommandsManager;
        this.playersCommandsManager = playersCommandsManager;
        this.adminService = adminService;
        this.adminChatService = adminChatService;
        this.globalChatService = globalChatService;
        this.managerMsgSender = managerMsgSender;
    }

    public void manageCommand(Update update) {
        String command = update.message().text().trim().substring(1).toUpperCase();
        try {
            Commands.Attribute attribute = Commands.AllCommands.valueOf(command).getAttribute();
            switch (attribute) {
                case GLOBALADMIN -> checkAndSendGlobalAdminsCommand(update, command);
                case PLAYER -> checkAndSendPlayersCommand(update, command);
                case ADMIN -> checkAndSendAdminsCommand(update, command);
            }
        } catch (IllegalArgumentException e) {
            managerMsgSender.sendInvalidCommandMsg(update);
        }
    }

    private void checkAndSendPlayersCommand(Update update, String command) {
        if (globalChatService.findById(update.message().chat().id()).isPresent) {
            playersCommandsManager.doSome(update, command);
        } else managerMsgSender.sendNotGlobalChatMsg(update);
    }

    private void checkAndSendAdminsCommand(Update update, String command) {
        if (adminChatService.findById(update.message().chat().id()).isPresent) {
            checkAndSendGlobalAdminsCommand(update, command);
        } else managerMsgSender.sendNotAdminChatMsg(update);
    }

    private void checkAndSendGlobalAdminsCommand(Update update, String command) {
        if (adminService.findById(update.message().from().id()).isPresent) {
            adminsCommandsManager.doSome(update, command);
        } else managerMsgSender.sendNotAdminMsg(update);
    }
}

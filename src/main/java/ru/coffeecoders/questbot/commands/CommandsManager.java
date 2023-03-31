package ru.coffeecoders.questbot.commands;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;

@Component
public class CommandsManager {
    private final AdminsCommandsManager adminsCommandsManager;
    private final PlayersCommandsManager playersCommandsManager;
    private final AdminService adminService;
    private final AdminChatService adminChatService;
    private final GlobalChatService globalChatService;
    private final CommandsManagerMsgSender managerMsgSender;

    private long chatId;

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
        String textCommand = update.message().text().trim().substring(1).toUpperCase();
        try {
            Commands.Command cmd = Commands.Command.valueOf(textCommand);
            chatId = update.message().chat().id();
            manageCommandByAttribute(update, cmd);
        } catch (IllegalArgumentException e) {
            //TODO  managerMsgSender.sendInvalidCommandMsg(chatId);
        }
    }

    private void manageCommandByAttribute(Update update, Commands.Command cmd) {
        Commands.Attribute attribute = cmd.getAttribute();
        switch (attribute) {
            case GLOBALADMIN -> checkAndSendGlobalAdminsCommand(update, cmd);
            case PLAYER -> checkAndSendPlayersCommand(update, cmd);
            case ADMIN -> checkAndSendAdminsCommand(update, cmd);
        }
    }

    private void checkAndSendPlayersCommand(Update update, Commands.Command command) {
        if (globalChatService.findById(chatId).isPresent) {
            //TODO playersCommandsManager.doSome(update, command);
        } else {
            //TODO managerMsgSender.sendNotGlobalChatMsg(chatId);
        }
    }

    private void checkAndSendAdminsCommand(Update update, Commands.Command command) {
        if (adminChatService.findById(chatId).isPresent) {
            checkAndSendGlobalAdminsCommand(update, command);
        } else {
            //TODO managerMsgSender.sendNotAdminChatMsg(chatId);
        }
    }

    private void checkAndSendGlobalAdminsCommand(Update update, Commands.Command command) {
        if (adminService.findById(update.message().from().id()).isPresent) {
            //TODO adminsCommandsManager.doSome(update, command);
        } else {
            //TODO managerMsgSender.sendNotAdminMsg(chatId);
        }
    }
}

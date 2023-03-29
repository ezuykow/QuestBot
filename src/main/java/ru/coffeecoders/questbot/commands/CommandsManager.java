package ru.coffeecoders.questbot.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//TODO поменять методы классов AdminsCommandsManager, PlayersCommandsManager, AdminService, AdminChatService, GlobalChatService, когда они будут готовы

@Component
public class CommandsManager {
    private final AdminsCommandsManager adminsCommandsManager;
    private final PlayersCommandsManager playersCommandsManager;
    private final Commands commands;
    private final AdminService adminService;
    private final AdminChatService adminChatService;
    private final GlobalChatService globalChatService;
    private final Reports reports;

    public CommandsManager(AdminsCommandsManager adminsCommandsManager, PlayersCommandsManager playersCommandsManager, TelegramBot telegramBot, Commands commands, AdminService adminService, AdminChatService adminChatService, GlobalChatService globalChatService, Reports reports) {
        this.adminsCommandsManager = adminsCommandsManager;
        this.playersCommandsManager = playersCommandsManager;
        this.telegramBot = telegramBot;
        this.commands = commands;
        this.adminService = adminService;
        this.adminChatService = adminChatService;
        this.globalChatService = globalChatService;
        this.reports = reports;
    }

    public void manageCommand(Update update) {
        String command = update.message().text();
        if (commands.getAllGlobalAdminsCommands().contains(command)) {
            return adminsCommandsManager.doSome(update);
        }
        if (commands.getAllAdminsCommands().contains(command)) {
            return checkAdminAndChatId(update);
        }
        if (commands.getAllPlayersCommands().contains(command)) {
            return checkPlayerChatId(update);
        }
        telegramBot.execute(new SendMessage(update.message().chat().id(), reports.INVALID_COMMAND));

    }

    private int checkPlayerChatId(Update update) {
        if (globalChatService.isGlobalChat(update.message().chat().id())) {
            return playersCommandsManager.doSome(update);
        } else {
            telegramBot.execute(new SendMessage(update.message().chat().id(), reports.NOT_GLOBAL_CHAT));
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }
    }

    private int checkAdminAndChatId(Update update) {
        if (adminService.isAdmin(update.message().from().id())) {
            if (adminChatService.isAdminChat(update.message().chat().id())) {
                return adminsCommandsManager.doSome(update);
            } else {
                telegramBot.execute(new SendMessage(update.message().chat().id(), reports.NOT_ADMIN_CHAT));
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }
        } else {
            telegramBot.execute(new SendMessage(update.message().chat().id(), reports.NOT_ADMIN));
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }
    }
}

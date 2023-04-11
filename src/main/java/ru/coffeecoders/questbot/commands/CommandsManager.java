package ru.coffeecoders.questbot.commands;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.msg.senders.CommandsManagerMsgSender;
import ru.coffeecoders.questbot.services.AdminChatService;
import ru.coffeecoders.questbot.services.AdminService;
import ru.coffeecoders.questbot.services.GlobalChatService;

/**
 * @author anna
 * Класс определяет тип отправленной команды, проверяет, в каком чате (админском или игровом)
 * и кем (админом или игроком) она была отправлена
 */
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

    /**
     * Инициализирует поле chatId;
     * Проверяет, существует ли команда и если это так, создает объект {@link Commands.Command}
     * Иначе вызывет метод {@link CommandsManagerMsgSender#sendInvalidCmdMsg}, оповещающий об ошибке
     *
     * @param update - апдейт (текст команды)
     */
    public void manageCommand(Update update) {
        String textCommand = update.message().text().trim().substring(1).toUpperCase();
        chatId = update.message().chat().id();
        try {
            Commands.Command cmd = Commands.Command.valueOf(textCommand);
            manageCommandByAttribute(update, cmd);
        } catch (IllegalArgumentException e) {
            managerMsgSender.sendInvalidCmdMsg(chatId);
        }
    }

    /**
     * Получает поле {@link Commands.Command#attribute} и пробрасывает апдейт в соответствующий класс
     *
     * @param update - апдейт (текст команды)
     * @param cmd    - команда (объект {@link Commands.Command})
     */
    private void manageCommandByAttribute(Update update, Commands.Command cmd) {
        Commands.Attribute attribute = cmd.getAttribute();
        switch (attribute) {
            case GLOBALADMIN -> checkAndSendGlobalAdminsCommand(update, cmd);
            case PLAYER -> checkAndSendPlayersCommand(update, cmd);
            case ADMIN -> checkAndSendAdminsCommand(update, cmd);
        }
    }

    private void checkAndSendPlayersCommand(Update update, Commands.Command cmd) {
        if (globalChatService.findById(chatId).isPresent()) {
            //TODO playersCommandsManager.doSome(update, cmd);
        } else {
            managerMsgSender.sendAdminCmdInGlobalChatMsg(chatId);
        }
    }

    private void checkAndSendAdminsCommand(Update update, Commands.Command cmd) {
        if (adminChatService.findById(chatId).isPresent()) {
            checkAndSendGlobalAdminsCommand(update, cmd);
        } else {
            managerMsgSender.sendGameCmdInAdminChatMsg(chatId);
        }
    }

    private void checkAndSendGlobalAdminsCommand(Update update, Commands.Command cmd) {
        if (adminService.findById(update.message().from().id()).isPresent()) {
            //TODO adminsCommandsManager.doSome(update, cmd);
        } else {
            managerMsgSender.sendNotAdminMsg(chatId);
        }
    }
}

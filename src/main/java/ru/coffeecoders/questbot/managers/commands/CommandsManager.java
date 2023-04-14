
package ru.coffeecoders.questbot.managers.commands;

import com.pengrad.telegrambot.model.Update;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.commands.Commands;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.validators.ChatAndUserIdValidator;


/**
 * @author anna
 * Класс определяет тип отправленной команды, проверяет, в каком чате (админском или игровом)
 * и кем (админом или игроком) она была отправлена
 */
@Component
public class CommandsManager {
    private final AdminsCommandsManager adminsCommandsManager;
    private final PlayersCommandsManager playersCommandsManager;
    private final ChatAndUserIdValidator validator;
    private final MessageSender msgSender;
    private final Environment env;
    private long chatId;

    public CommandsManager(AdminsCommandsManager adminsCommandsManager,
                           PlayersCommandsManager playersCommandsManager,
                           ChatAndUserIdValidator validator,
                           MessageSender msgSender,
                           Environment env) {
        this.adminsCommandsManager = adminsCommandsManager;
        this.playersCommandsManager = playersCommandsManager;
        this.validator = validator;
        this.msgSender = msgSender;
        this.env = env;
    }

    /**
     * Инициализирует поле chatId;
     * Проверяет, существует ли команда и если это так, создает объект {@link Commands.Command}
     * Иначе выведет сообщение, оповещающее об ошибке
     *
     * @param update - апдейт (текст команды)
     */
    public void manageCommand(Update update) {
        chatId = update.message().chat().id();
        String textCommand = update.message().text().trim().substring(1).toUpperCase();
        try {
            Commands.Command cmd = Commands.Command.valueOf(textCommand);
            manageCommandByAttribute(update, cmd);
        } catch (IllegalArgumentException e) {
            msgSender.send(chatId, env.getProperty("messages.admins.invalidMsg"));
        }
    }

    /**
     * Получает атрибут команды и пробрасывает апдейт в соответствующий класс
     *
     * @param update апдейт (текст команды)
     * @param cmd    команда (объект {@link Commands.Command})
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
        if (validator.isGlobalChat(chatId)) {
            playersCommandsManager.manageCommand(update, cmd);
        } else {
            msgSender.send(chatId, env.getProperty("messages.admins.gameCmdInAdminChat"));
        }
    }

    private void checkAndSendAdminsCommand(Update update, Commands.Command cmd) {
        if (validator.isAdminChat(chatId)) {
            checkAndSendGlobalAdminsCommand(update, cmd);
        } else {
            msgSender.send(chatId, env.getProperty("messages.admins.adminCmdInGlobalChat"));
        }
    }

    private void checkAndSendGlobalAdminsCommand(Update update, Commands.Command cmd) {
        if (validator.isAdmin(update.message().from().id())) {
            adminsCommandsManager.manageCommand(update, cmd);
        } else {
            msgSender.send(chatId, env.getProperty("messages.admins.cmdSendByNotAdmin"));
        }
    }
}
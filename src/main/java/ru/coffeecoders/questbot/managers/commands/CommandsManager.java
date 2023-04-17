
package ru.coffeecoders.questbot.managers.commands;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
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
     * Проверяет, существует ли команда и если это так, создает объект {@link Command}
     * Иначе выведет сообщение, оповещающее об ошибке
     *
     * @param update - апдейт (текст команды)
     */
    public void manageCommand(ExtendedUpdate update) {
        chatId = update.getMessageChatId();
        String textCommand = getTextCommand(update);
        try {
            Command cmd = Command.valueOf(textCommand);
            manageCommandByAttribute(update, cmd);
        } catch (IllegalArgumentException e) {
            msgSender.send(chatId, env.getProperty("messages.admins.invalidMsg"));
        }
    }

    /**
     * Получает атрибут команды и пробрасывает апдейт в соответствующий класс
     *
     * @param update апдейт (текст команды)
     * @param cmd    команда (объект {@link Command})
     */
    private void manageCommandByAttribute(ExtendedUpdate update, Command cmd) {
        Command.Attribute attribute = cmd.getAttribute();
        switch (attribute) {
            case GLOBALADMIN -> checkAndSendGlobalAdminsCommand(update, cmd);
            case PLAYER -> checkAndSendPlayersCommand(update, cmd);
            case ADMIN -> checkAndSendAdminsCommand(update, cmd);
        }
    }

    private void checkAndSendPlayersCommand(ExtendedUpdate update, Command cmd) {
        if (validator.isGlobalChat(chatId)) {
            playersCommandsManager.manageCommand(update, cmd);
        } else {
            msgSender.send(chatId, env.getProperty("messages.admins.gameCmdInAdminChat"));
        }
    }

    private void checkAndSendAdminsCommand(ExtendedUpdate update, Command cmd) {
        if (validator.isAdminChat(chatId)) {
            checkAndSendGlobalAdminsCommand(update, cmd);
        } else {
            msgSender.send(chatId, env.getProperty("messages.admins.adminCmdInGlobalChat"));
        }
    }

    private void checkAndSendGlobalAdminsCommand(ExtendedUpdate update, Command cmd) {
        if (validator.isAdmin(update.getMessageFromUserId())) {
            adminsCommandsManager.manageCommand(update, cmd);
        } else {
            msgSender.send(chatId, env.getProperty("messages.admins.cmdSendByNotAdmin"));
        }
    }

    private String getTextCommand(ExtendedUpdate update) {
        String text = update.getMessageText();
        return text.trim().
                substring(1, (text.contains("@") ? text.indexOf("@") : text.length()))
                .toUpperCase();
    }
}
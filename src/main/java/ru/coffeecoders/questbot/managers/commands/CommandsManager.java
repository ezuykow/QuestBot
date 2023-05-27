
package ru.coffeecoders.questbot.managers.commands;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.validators.ChatAndUserValidator;


/**
 * @author anna
 * Класс определяет тип отправленной команды, проверяет, в каком чате (админском или игровом)
 * и кем (админом или игроком) она была отправлена
 */
@Component
public class CommandsManager {

    private final OwnerCommandsManager ownerCommandsManager;
    private final AdminsCommandsManager adminsCommandsManager;
    private final ChatAndUserValidator validator;
    private final MessageSender msgSender;
    private final Messages messages;
    private long chatId;

    public CommandsManager(OwnerCommandsManager ownerCommandsManager, AdminsCommandsManager adminsCommandsManager,
                           ChatAndUserValidator validator, MessageSender msgSender, Messages messages)
    {
        this.ownerCommandsManager = ownerCommandsManager;
        this.adminsCommandsManager = adminsCommandsManager;
        this.validator = validator;
        this.msgSender = msgSender;
        this.messages = messages;
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
            Command cmd;
            if (textCommand.isEmpty()) {
                cmd = Command.EMPTY;
            } else {
                cmd = Command.valueOf(textCommand);
            }
            manageCommandByAttribute(update, cmd);
        } catch (IllegalArgumentException e) {
            msgSender.send(chatId, messages.invalidMsg());
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
            case OWNER -> checkAndSendOwnersCommand(update, cmd);
            case ADMIN -> checkAndSendAdminsCommand(update, cmd);
            case GLOBALADMIN -> checkAndSendGlobalAdminsCommand(update, cmd);
        }
    }

    /**
     * @author ezuykow
     */
    private void checkAndSendOwnersCommand(ExtendedUpdate update, Command cmd){
        deleteMessageWithCommand(update);
        if (validator.isOwner(update.getMessageFromUserId())) {
            ownerCommandsManager.manageCommand(update.getMessageChatId(), cmd);
        } else {
            msgSender.send(chatId, messages.isOwnerCommand());
        }
    }

    private void checkAndSendAdminsCommand(ExtendedUpdate update, Command cmd) {
        if (validator.isAdminChat(chatId)) {
            checkAndSendGlobalAdminsCommand(update, cmd);
        } else {
            msgSender.send(chatId, messages.adminCmdInGlobalChat());
        }
    }

    /**
     * @author anna
     * @Redact: ezuykow
     */
    private void checkAndSendGlobalAdminsCommand(ExtendedUpdate update, Command cmd) {
        if (cmd != Command.REGTEAM) {
            deleteMessageWithCommand(update);
        }
        if (validator.isAdmin(update.getMessageFromUserId())) {
            adminsCommandsManager.manageCommand(update, cmd);
        } else {
            msgSender.send(chatId, messages.cmdSendByNotAdmin());
        }
    }

    /**
     * @author ezuykow
     */
    private String getTextCommand(ExtendedUpdate update) {
        String text = update.getMessageText();
        if (text.length() > 1) {
            return text.trim().
                    substring(1, (text.contains("@") ? text.indexOf("@") : text.length()))
                    .toUpperCase();
        } else {
            return "";
        }
    }

    /**
     * @author ezuykow
     */
    private void deleteMessageWithCommand(ExtendedUpdate update) {
        msgSender.sendDelete(update.getMessageChatId(), update.getMessageId());
    }
}
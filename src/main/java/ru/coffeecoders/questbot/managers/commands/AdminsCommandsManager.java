package ru.coffeecoders.questbot.managers.commands;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.actions.commands.AdminsCommandsActions;
import ru.coffeecoders.questbot.models.ExtendedUpdate;

@Component
public class AdminsCommandsManager {

    private final AdminsCommandsActions cmdActions;

    public AdminsCommandsManager(AdminsCommandsActions cmdActions) {
        this.cmdActions = cmdActions;
    }

    /**
     * @author anna
     * <br>Redact ezuykow
     */
    public void manageCommand(ExtendedUpdate update, Command cmd) {
        long senderAdminId = update.getMessageFromUserId();
        long chatId = update.getMessageChatId();
        switch (cmd) {
            case SHOWQUESTIONS -> cmdActions.performShowQuestionsCmd(senderAdminId, chatId);
            case STOPBOT -> cmdActions.performStopBotCmd();
        }
    }
}


package ru.coffeecoders.questbot.managers.commands;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.actions.commands.OwnerCommandsActions;
import ru.coffeecoders.questbot.models.ExtendedUpdate;

/**
 * @author ezuykow
 */
@Component
public class OwnerCommandsManager {

    private final OwnerCommandsActions actions;

    public OwnerCommandsManager(OwnerCommandsActions actions) {
        this.actions = actions;
    }

    /**
     *
     * @author ezuykow
     */
    public void manageCommand(ExtendedUpdate update, Command cmd) {
        long chatId = update.getMessageChatId();
        switch (cmd) {
            case START -> actions.validateAndPerformStartCmd(chatId);
            case ADMINON -> actions.validateAndPerformAdminOnCmd(chatId);
            case ADMINOFF -> actions.validateAndPerformAdminOffCmd(chatId);
            case PROMOTE -> actions.validateAndPerformPromoteCmd(chatId);
            case DEMOTE -> actions.validateAndPerformDemoteCmd(chatId);
        }
    }
}

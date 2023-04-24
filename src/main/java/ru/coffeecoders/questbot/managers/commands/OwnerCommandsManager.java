package ru.coffeecoders.questbot.managers.commands;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.actions.commands.OwnerCommandsActions;

/**
 * @author ezuykow
 */
@Component
public class OwnerCommandsManager {

    private final OwnerCommandsActions actions;

    public OwnerCommandsManager(OwnerCommandsActions actions) {
        this.actions = actions;
    }

    //-----------------API START-----------------

    /**
     * Исходя из пришедшей команды вызывает соответствующий метод {@link OwnerCommandsActions}
     * @param chatId id чата, с которого пришла команда
     * @param cmd команда
     * @author ezuykow
     */
    public void manageCommand(long chatId, Command cmd) {
        switch (cmd) {
            case START -> actions.validateAndPerformStartCmd(chatId);
            case ADMINON -> actions.validateAndPerformAdminOnCmd(chatId);
            case ADMINOFF -> actions.validateAndPerformAdminOffCmd(chatId);
            case PROMOTE -> actions.validateAndPerformPromoteCmd(chatId);
            case DEMOTE -> actions.validateAndPerformDemoteCmd(chatId);
        }
    }

    //-----------------API END-----------------

}

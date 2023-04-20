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
        switch (cmd) {
            case START -> cmdActions.performStartCmd(update);
            case SHOWQUESTIONS -> cmdActions.performShowQuestionsCmd(update);
            case ADMINON -> cmdActions.performAdminOnCmd(update);
            case ADMINOFF -> cmdActions.performAdminOffCmd(update);
            case PROMOTE -> cmdActions.performPromoteCmd(update);
            case STOPBOT -> cmdActions.performStopBotCmd();
        }
    }
}


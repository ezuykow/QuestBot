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

    public void manageCommand(ExtendedUpdate update, Command cmd) {

        switch (cmd) {
            case START -> cmdActions.performStartCmd(update);
            case SHOWQUESTIONS -> cmdActions.performShowQuestionsCmd(update);
            case ADMINON -> cmdActions.performAdminOnCmd(update);
        }
    }
}


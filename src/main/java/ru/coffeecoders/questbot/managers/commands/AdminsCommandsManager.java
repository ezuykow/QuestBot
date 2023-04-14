package ru.coffeecoders.questbot.managers.commands;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.commands.Commands;
import ru.coffeecoders.questbot.commands.actions.AdminsCommandsActions;
import ru.coffeecoders.questbot.models.ExtendedUpdate;

@Component
public class AdminsCommandsManager {

    private final AdminsCommandsActions cmdActions;

    public AdminsCommandsManager(AdminsCommandsActions cmdActions) {
        this.cmdActions = cmdActions;
    }

    public void manageCommand(ExtendedUpdate update, Commands.Command cmd) {

        switch (cmd) {
            case START -> cmdActions.performStartCmd(update);
            case SHOWQUESTIONS -> cmdActions.performShowQuestionsCmd(update);
        }
    }
}


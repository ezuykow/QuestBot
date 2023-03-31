package ru.coffeecoders.questbot.commands.admins;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.commands.Commands;

@Component
public class AdminsCommandsManager {

    private final AdminsCommandActions cmdActions;

    public AdminsCommandsManager(AdminsCommandActions cmdActions) {
        this.cmdActions = cmdActions;
    }

    public void manageCommand(Update update, Commands.Command cmd) {

        switch (cmd) {
            case NEWADMIN -> cmdActions.performNewAdminCmd(update);
            case NEWGAME -> cmdActions.performNewGameCmd(update);
            case START -> cmdActions.performStartCmd(update);
            case MAIN -> cmdActions.performStartCmd();
            case ADDQUESTION -> cmdActions.performNewAddQuestionCmd(update);
            case DELETEQUESTION -> cmdActions.performDeleteQuestionCmd(update);
            case EDITQUESTION -> cmdActions.performEditeQuestionCmd(update);
            case SHOWQUESTIONS ->  cmdActions.performShowQuestionCmd(update);
        }
    }
}



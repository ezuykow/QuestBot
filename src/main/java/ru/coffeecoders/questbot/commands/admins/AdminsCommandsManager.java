package ru.coffeecoders.questbot.commands.admins;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.commands.Commands;

@Component
public class AdminsCommandsManager {

    private final AdminsCommandsActions cmdActions;

    public AdminsCommandsManager(AdminsCommandsActions cmdActions) {
        this.cmdActions = cmdActions;
    }

    public void manageCommand(Update update, Commands.Command cmd) {

        switch (cmd) {
            case NEWADMIN -> cmdActions.performNewAdminCmd(update);
            case NEWGAME -> cmdActions.performNewGameCmd(update);
            case START -> cmdActions.performStartCmd(update);
            case MAIN -> cmdActions.performMainCmd(update);

            case QUESTIONSMENU -> cmdActions.performQuestionMenuCmd(update);
            //TODO добавление вопросов в List (где храним?)
            case ADDQUESTION -> cmdActions.performNewAddQuestionCmd(update);
            case SHOWQUESTIONS ->  cmdActions.performShowQuestionCmd(update);
            case EDITKEYBOARD ->cmdActions.performEditQuestionCmd(update);
        }
    }
}


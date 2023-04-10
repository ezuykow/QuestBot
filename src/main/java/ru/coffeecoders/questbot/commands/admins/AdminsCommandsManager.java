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
            case ADDQUESTION -> cmdActions.performNewAddQuestionCmd(update);
            case SHOWQUESTIONS ->  cmdActions.performShowQuestionCmd(update);
            case EDITKEYBOARD ->cmdActions.performEditQuestionCmd(update);

            case STARTTEAMMAKER -> cmdActions.performStarTeamMakerCmd(update.message().chat().id());
            case STARTGAME -> cmdActions.performStartGameCmd(update.message().chat().id());
            case STOPTGAME -> cmdActions.performStopGameCmd(update.message().chat().id());
        }
    }
}


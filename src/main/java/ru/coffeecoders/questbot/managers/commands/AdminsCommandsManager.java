package ru.coffeecoders.questbot.managers.commands;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.actions.commands.AdminsCommandsActions;
import ru.coffeecoders.questbot.managers.NewGameManager;
import ru.coffeecoders.questbot.models.ExtendedUpdate;

@Component
public class AdminsCommandsManager {

    private final AdminsCommandsActions cmdActions;
    private final NewGameManager newGameManager;

    public AdminsCommandsManager(AdminsCommandsActions cmdActions, NewGameManager newGameManager) {
        this.cmdActions = cmdActions;
        this.newGameManager = newGameManager;
    }

    /**
     * @author anna
     * <br>Redact ezuykow
     */
    public void manageCommand(ExtendedUpdate update, Command cmd) {
        long senderAdminId = update.getMessageFromUserId();
        long chatId = update.getMessageChatId();
        switch (cmd) {
            case EMPTY -> cmdActions.showMyCommands(chatId);
            case REGTEAM -> cmdActions.performRegTeamCmd(update.getMessageId(), chatId, senderAdminId);
            case INFO -> cmdActions.showInfo(chatId);
            case QUESTIONS -> cmdActions.showQuestions(chatId);
            case SHOWGAMES -> cmdActions.performShowGamesCmd(senderAdminId, chatId);
            case SHOWQUESTIONS -> cmdActions.performShowQuestionsCmd(senderAdminId, chatId);
            case NEWGAME -> newGameManager.startCreatingGame(senderAdminId, chatId);
            case DELETECHAT -> cmdActions.performDeleteChatCmd(chatId);
            case PREPAREGAME -> cmdActions.performPrepareGameCmd(senderAdminId, chatId);
            case DROPPREPARE -> cmdActions.performDropPrepareGameCmd(senderAdminId, chatId);
            case STARTGAME -> cmdActions.performStartGameCmd(senderAdminId, chatId);
            case DROPGAME -> cmdActions.performDropGameCmd(senderAdminId, chatId);
        }
    }
}


package ru.coffeecoders.questbot.actions.commands;

import com.pengrad.telegrambot.model.User;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.managers.ApplicationShutdownManager;
import ru.coffeecoders.questbot.managers.BlockingManager;
import ru.coffeecoders.questbot.managers.RestrictingManager;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.viewers.GamesViewer;
import ru.coffeecoders.questbot.viewers.QuestionsViewer;

@Component
public class AdminsCommandsActions {

    private final GamesViewer gamesViewer;
    private final QuestionsViewer questionsViewer;
    private final BlockingManager blockingManager;
    private final RestrictingManager restrictingManager;
    private final ApplicationShutdownManager applicationShutdownManager;
    private final MessageSender msgSender;
    private final Environment env;

    private AdminsCommandsActions(GamesViewer gamesViewer, MessageSender msgSender, QuestionsViewer questionsViewer,
                                  ApplicationShutdownManager applicationShutdownManager,
                                  BlockingManager blockingManager, RestrictingManager restrictingManager,
                                  Environment env)
    {
        this.gamesViewer = gamesViewer;
        this.msgSender = msgSender;
        this.questionsViewer = questionsViewer;
        this.applicationShutdownManager = applicationShutdownManager;
        this.blockingManager = blockingManager;
        this.restrictingManager = restrictingManager;
        this.env = env;
    }

    public void performShowGamesCmd(long senderAdminId, long chatId) {
        blockAndRestrictChat(chatId, senderAdminId, env.getProperty("messages.admins.startGamesView"));
        gamesViewer.viewGames(chatId);
    }

    /**
     * @author ezuykow
     */
    public void performShowQuestionsCmd(long senderAdminId, long chatId) {
        blockAndRestrictChat(chatId, senderAdminId, env.getProperty("messages.admins.startQuestionView"));
        questionsViewer.viewQuestions(chatId);
    }

    /**
     * @author ezuykow
     */
    public void performStopBotCmd() {
        msgSender.sendStopBot();
        applicationShutdownManager.stopBot();
    }

    private void blockAndRestrictChat(long chatId, long senderAdminId, String cause) {
        blockingManager.blockAdminChatByAdmin(chatId, senderAdminId);
        restrictingManager.restrictMembers(chatId, senderAdminId);
        msgSender.send(chatId,
                buildName(chatId, senderAdminId) + cause);
    }

    private String buildName(long chatId, long senderAdminId) {
        User user = msgSender.getChatMember(chatId, senderAdminId);
        return (user.lastName() == null)
                ? user.firstName()
                : user.firstName() + " " + user.lastName();
    }
}

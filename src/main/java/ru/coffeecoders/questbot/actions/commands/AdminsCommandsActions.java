package ru.coffeecoders.questbot.actions.commands;

import com.pengrad.telegrambot.model.User;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.managers.ApplicationShutdownManager;
import ru.coffeecoders.questbot.managers.BlockingManager;
import ru.coffeecoders.questbot.managers.RestrictingManager;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.viewers.QuestionsViewer;

@Component
public class AdminsCommandsActions {

    private final ApplicationShutdownManager applicationShutdownManager;
    private final QuestionsViewer questionsViewer;
    private final BlockingManager blockingManager;
    private final RestrictingManager restrictingManager;
    private final MessageSender msgSender;
    private final Environment env;

    private AdminsCommandsActions(MessageSender msgSender, QuestionsViewer questionsViewer,
                                  ApplicationShutdownManager applicationShutdownManager,
                                  BlockingManager blockingManager, RestrictingManager restrictingManager,
                                  Environment env)
    {
        this.msgSender = msgSender;
        this.questionsViewer = questionsViewer;
        this.applicationShutdownManager = applicationShutdownManager;
        this.blockingManager = blockingManager;
        this.restrictingManager = restrictingManager;
        this.env = env;
    }

    /**
     * @author ezuykow
     */
    public void performShowQuestionsCmd(long senderAdminId, long chatId) {
        msgSender.send(chatId,
                buildName(chatId, senderAdminId) + env.getProperty("messages.admins.startQuestionView"));
        blockingManager.blockAdminChatByAdmin(chatId, senderAdminId);
        restrictingManager.restrictMembers(chatId, senderAdminId);
        questionsViewer.viewQuestions(chatId);
    }

    /**
     * @author ezuykow
     */
    public void performStopBotCmd() {
        msgSender.sendStopBot();
        applicationShutdownManager.stopBot();
    }

    private String buildName(long chatId, long senderAdminId) {
        User user = msgSender.getChatMember(chatId, senderAdminId);
        return (user.lastName() == null)
                ? user.firstName()
                : user.firstName() + " " + user.lastName();
    }
}

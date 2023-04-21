package ru.coffeecoders.questbot.actions.commands;

import com.pengrad.telegrambot.model.ChatPermissions;
import com.pengrad.telegrambot.model.User;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.managers.ApplicationShutdownManager;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.services.AdminChatMembersService;
import ru.coffeecoders.questbot.services.AdminChatService;
import ru.coffeecoders.questbot.services.AdminService;
import ru.coffeecoders.questbot.viewers.QuestionsViewer;

import java.util.Arrays;

@Component
public class AdminsCommandsActions {

    private final ApplicationShutdownManager applicationShutdownManager;
    private final AdminService adminService;
    private final AdminChatService adminChatService;
    private final AdminChatMembersService adminChatMembersService;
    private final QuestionsViewer questionsViewer;
    private final MessageSender msgSender;
    private final Environment env;

    private AdminsCommandsActions(MessageSender msgSender, QuestionsViewer questionsViewer,
                                  ApplicationShutdownManager applicationShutdownManager,
                                  AdminService adminService, AdminChatService adminChatService, AdminChatMembersService adminChatMembersService, Environment env)
    {
        this.msgSender = msgSender;
        this.questionsViewer = questionsViewer;
        this.applicationShutdownManager = applicationShutdownManager;
        this.adminService = adminService;
        this.adminChatService = adminChatService;
        this.adminChatMembersService = adminChatMembersService;
        this.env = env;
    }

    /**
     * @author ezuykow
     */
    public void performShowQuestionsCmd(long senderAdminId, long chatId) {
        msgSender.send(chatId,
                buildName(chatId, senderAdminId) + env.getProperty("messages.admins.startQuestionView"));
        blockAdminChatByThisAdmin(senderAdminId, chatId);
        restrictOtherChatMembers(senderAdminId, chatId);
        questionsViewer.viewQuestions(chatId);
    }

    /**
     * @author ezuykow
     */
    public void performStopBotCmd() {
        msgSender.sendStopBot();
        applicationShutdownManager.stopBot();
    }

    /**
     * @author ezuykow
     */
    public void blockAdminChatByThisAdmin(long senderAdminId, long chatId) {
        AdminChat currentAdminChat = adminChatService.findById(chatId).get();
        currentAdminChat.setBlockedByAdminId(senderAdminId);
        adminChatService.save(currentAdminChat);
    }

    /**
     * @author ezuykow
     */
    private void restrictOtherChatMembers(long senderAdminId, long chatId) {
        ChatPermissions permissions = new ChatPermissions()
                .canSendMessages(false)
                .canSendOtherMessages(false);

        Arrays.stream(adminChatMembersService.findByChatId(chatId).get().getMembers())
                .filter(id -> (id != senderAdminId) || (id != adminService.getOwner().getTgAdminUserId()))
                .forEach(id -> msgSender.sendRestrictChatMember(chatId, id, permissions));
    }

    private String buildName(long chatId, long senderAdminId) {
        User user = msgSender.getChatMember(chatId, senderAdminId);
        return (user.lastName() == null)
                ? user.firstName()
                : user.firstName() + " " + user.lastName();
    }
}

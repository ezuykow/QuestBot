package ru.coffeecoders.questbot.actions.commands;

import com.pengrad.telegrambot.model.ChatPermissions;
import com.pengrad.telegrambot.model.User;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.managers.ApplicationShutdownManager;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.services.AdminChatMembersService;
import ru.coffeecoders.questbot.services.AdminService;
import ru.coffeecoders.questbot.utils.BlockAdminChat;
import ru.coffeecoders.questbot.viewers.QuestionsViewer;

import java.util.Arrays;

@Component
public class AdminsCommandsActions {

    private final ApplicationShutdownManager applicationShutdownManager;
    private final AdminService adminService;
    private final AdminChatMembersService adminChatMembersService;
    private final QuestionsViewer questionsViewer;
    private final BlockAdminChat blockAdminChat;
    private final MessageSender msgSender;
    private final Environment env;

    private AdminsCommandsActions(MessageSender msgSender, QuestionsViewer questionsViewer,
                                  ApplicationShutdownManager applicationShutdownManager,
                                  AdminService adminService, AdminChatMembersService adminChatMembersService,
                                  BlockAdminChat blockAdminChat, Environment env)
    {
        this.msgSender = msgSender;
        this.questionsViewer = questionsViewer;
        this.applicationShutdownManager = applicationShutdownManager;
        this.adminService = adminService;
        this.adminChatMembersService = adminChatMembersService;
        this.blockAdminChat = blockAdminChat;
        this.env = env;
    }

    /**
     * @author ezuykow
     */
    public void performShowQuestionsCmd(long senderAdminId, long chatId) {
        msgSender.send(chatId,
                buildName(chatId, senderAdminId) + env.getProperty("messages.admins.startQuestionView"));
        blockAdminChat.validateAndBlockAdminChatByAdmin(chatId, senderAdminId);
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
    private void restrictOtherChatMembers(long senderAdminId, long chatId) {
        ChatPermissions permissions = new ChatPermissions()
                .canSendMessages(false)
                .canSendOtherMessages(false);

        Arrays.stream(adminChatMembersService.findByChatId(chatId).get().getMembers())
                .filter(id -> (id != senderAdminId) && (id != adminService.getOwner().getTgAdminUserId()))
                .forEach(id -> msgSender.sendRestrictChatMember(chatId, id, permissions));
    }

    private String buildName(long chatId, long senderAdminId) {
        User user = msgSender.getChatMember(chatId, senderAdminId);
        return (user.lastName() == null)
                ? user.firstName()
                : user.firstName() + " " + user.lastName();
    }
}

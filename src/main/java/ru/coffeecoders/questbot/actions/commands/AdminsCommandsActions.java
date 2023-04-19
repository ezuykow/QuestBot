package ru.coffeecoders.questbot.actions.commands;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Admin;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.services.AdminChatService;
import ru.coffeecoders.questbot.services.AdminService;
import ru.coffeecoders.questbot.services.GlobalChatService;
import ru.coffeecoders.questbot.viewers.QuestionsViewer;

import java.util.*;

@Component
public class AdminsCommandsActions {

    private final MessageSender msgSender;
    private final QuestionsViewer questionsViewer;
    private final AdminChatService adminChatService;
    private final AdminService adminService;
    private final GlobalChatService globalChatService;
    private final Environment env;

    private AdminsCommandsActions(MessageSender msgSender, QuestionsViewer questionsViewer, AdminChatService adminChatService, AdminService adminService, GlobalChatService globalChatService, Environment env) {
        this.msgSender = msgSender;
        this.questionsViewer = questionsViewer;
        this.adminChatService = adminChatService;
        this.adminService = adminService;
        this.globalChatService = globalChatService;
        this.env = env;
    }

    /**
     *
     * @param update
     * @author ezuykow
     */
    public void performStartCmd(ExtendedUpdate update) {
        long chatId = update.getMessageChatId();
        globalChatService.save(new GlobalChat(chatId));
        msgSender.send(chatId, env.getProperty("messages.welcome"));
    }

    /**
     *
     * @param update
     * @author ezuykow
     */
    public void performShowQuestionsCmd(ExtendedUpdate update) {
        questionsViewer.viewQuestions(update);
    }

    /**
     *
     * @param update
     * @author ezuykow
     */
    public void performAdminOnCmd(ExtendedUpdate update) {
        final long chatId = update.getMessageChatId();
        final long userId = update.getMessageFromUserId();
        Optional<Admin> owner = adminService.findById(update.getMessageFromUserId())
                .filter(Admin::isOwner);
        if (owner.isPresent()) {
            final AdminChat adminChat =
                    new AdminChat(
                            chatId,
                            new HashSet<>(Collections.singleton(owner.get()))
                    );
            globalChatService.deleteById(chatId);
            adminChatService.save(adminChat);
            msgSender.send(chatId, env.getProperty("messages.admins.chatIsAdminNow"));
        } else {
            msgSender.send(chatId, env.getProperty("messages.admins.isOwnerCommand"));
        }
    }

    /**
     * Удаляет чат по chatId из апдейта из админских чатов и добавляет его в глобальные чаты
     * @param update апдейт с chatId
     * @author ezuykow
     */
    public void performAdminOffCmd(ExtendedUpdate update) {
        long chatId = update.getMessageChatId();
        Optional<Admin> owner = adminService.findById(update.getMessageFromUserId())
                .filter(Admin::isOwner);
        if (owner.isPresent()) {
            adminChatService.findById(chatId)
                    .ifPresent(adminChat -> deleteAdminChat(adminChat, chatId, update));
        } else {
            msgSender.send(chatId, env.getProperty("messages.admins.isOwnerCommand"));
        }
    }

    /**
     *
     * @param adminChat
     * @param chatId
     * @param update
     * @author ezuykow
     */
    private void deleteAdminChat(AdminChat adminChat, long chatId, ExtendedUpdate update) {
        adminChatService.delete(adminChat);
        deleteAllUselessAdmins(update);
        globalChatService.save(new GlobalChat(chatId));
        msgSender.send(chatId, env.getProperty("messages.admins.chatIsGlobalNow"));
    }

    /**
     *
     * @param update
     * @author ezuykow
     */
    private void deleteAllUselessAdmins(ExtendedUpdate update) {
        List<Admin> adminsToDelete = adminService.findAll()
                .stream()
                .filter(admin -> !admin.isOwner() && admin.getAdminChats().isEmpty())
                .toList();
        adminService.deleteAll(adminsToDelete);
    }
}

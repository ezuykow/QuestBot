package ru.coffeecoders.questbot.actions.commands;

import com.pengrad.telegrambot.model.User;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.util.ArrayUtil;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Admin;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.entities.AdminChatMembers;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.keyboards.PromoteUserKeyboard;
import ru.coffeecoders.questbot.managers.ApplicationShutdownManager;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.services.AdminChatMembersService;
import ru.coffeecoders.questbot.services.AdminChatService;
import ru.coffeecoders.questbot.services.AdminService;
import ru.coffeecoders.questbot.services.GlobalChatService;
import ru.coffeecoders.questbot.validators.ChatAndUserIdValidator;
import ru.coffeecoders.questbot.viewers.QuestionsViewer;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class AdminsCommandsActions {

    private final MessageSender msgSender;
    private final QuestionsViewer questionsViewer;
    private final AdminChatService adminChatService;
    private final AdminService adminService;
    private final GlobalChatService globalChatService;
    private final AdminChatMembersService adminChatMembersService;
    private final ApplicationShutdownManager applicationShutdownManager;
    private final ChatAndUserIdValidator validator;
    private final Environment env;

    private AdminsCommandsActions(MessageSender msgSender, QuestionsViewer questionsViewer,
                                  AdminChatService adminChatService, AdminService adminService,
                                  GlobalChatService globalChatService, AdminChatMembersService adminChatMembersService,
                                  ApplicationShutdownManager applicationShutdownManager,
                                  ChatAndUserIdValidator validator, Environment env)
    {
        this.msgSender = msgSender;
        this.questionsViewer = questionsViewer;
        this.adminChatService = adminChatService;
        this.adminService = adminService;
        this.globalChatService = globalChatService;
        this.adminChatMembersService = adminChatMembersService;
        this.applicationShutdownManager = applicationShutdownManager;
        this.validator = validator;
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
        Optional<Admin> getUserIfOwner = adminService.findById(update.getMessageFromUserId())
                .filter(Admin::isOwner);
        if (getUserIfOwner.isPresent()) {
            createAdminChat(chatId, getUserIfOwner.get());
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
        if (validator.isOwner(update.getMessageFromUserId())) {
            adminChatService.findById(chatId)
                    .ifPresent(adminChat -> deleteAdminChat(adminChat, chatId));
        } else {
            msgSender.send(chatId, env.getProperty("messages.admins.isOwnerCommand"));
        }
    }

    /**
     * @author ezuykow
     */
    public void performPromoteCmd(ExtendedUpdate update) {
        long chatId = update.getMessageChatId();
        if (validator.isOwner(update.getMessageFromUserId())) {
            Set<User> members = getNotAdminMembersInAdminChat(chatId);
            if (members.isEmpty()) {
                msgSender.send(chatId, env.getProperty("messages.admins.emptyPromotionList"));
            } else {
                msgSender.send(chatId, env.getProperty("messages.admins.promote"),
                        PromoteUserKeyboard.createKeyboard(members)
                );
            }
        } else {
            msgSender.send(chatId, env.getProperty("messages.admins.isOwnerCommand"));
        }
    }

    /**
     *
     * @author ezuykow
     */
    public void performStopBotCmd() {
        msgSender.sendStopBot();
        applicationShutdownManager.stopBot();
    }

    /**
     * @author ezuykow
     */
    private void deleteAdminChat(AdminChat adminChat, long chatId) {
        adminChatService.delete(adminChat);
        adminChatMembersService.deleteByChatId(chatId);
        deleteAllUselessAdmins();
        globalChatService.save(new GlobalChat(chatId));
        msgSender.send(chatId, env.getProperty("messages.admins.chatIsGlobalNow"));
    }

    /**
     * @author ezuykow
     */
    private void deleteAllUselessAdmins() {
        List<Admin> adminsToDelete = adminService.findAll()
                .stream()
                .filter(admin -> !admin.isOwner() && admin.getAdminChats().isEmpty())
                .toList();
        adminService.deleteAll(adminsToDelete);
    }

    /**
     * @author ezuykow
     */
    private void createAdminChat(long chatId, Admin owner) {
        final AdminChat adminChat =
                new AdminChat(
                        chatId,
                        new HashSet<>(Collections.singleton(owner))
                );
        createAdminChatMembers(chatId, owner);
        globalChatService.deleteById(chatId);
        adminChatService.save(adminChat);
    }

    /**
     * @author ezuykow
     */
    private void createAdminChatMembers(long chatId, Admin owner) {
        final AdminChatMembers adminChatMembers =
                new AdminChatMembers(
                        chatId,
                        new long[]{owner.getTgAdminUserId()}
                );
        adminChatMembersService.save(adminChatMembers);
    }

    /**
     * @return
     * @author ezuykow
     */
    private Set<User> getNotAdminMembersInAdminChat(long chatId) {
        Set<Long> membersIds = new HashSet<>(
                Set.of(ArrayUtils.toObject(adminChatMembersService.findByChatId(chatId).get().getMembers()))
        );
        membersIds.removeAll(adminChatService.findById(chatId).get().getAdmins()
                .stream().map(Admin::getTgAdminUserId).collect(Collectors.toSet()));
        return membersIds.stream().map(id -> msgSender.getChatMember(chatId, id)).collect(Collectors.toSet());
    }
}

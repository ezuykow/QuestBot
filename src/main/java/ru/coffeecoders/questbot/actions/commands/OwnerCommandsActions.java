package ru.coffeecoders.questbot.actions.commands;

import com.pengrad.telegrambot.model.User;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Admin;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.entities.AdminChatMembers;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.keyboards.PromoteOrDemoteUserKeyboard;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.services.AdminChatMembersService;
import ru.coffeecoders.questbot.services.AdminChatService;
import ru.coffeecoders.questbot.services.AdminService;
import ru.coffeecoders.questbot.services.GlobalChatService;
import ru.coffeecoders.questbot.validators.ChatAndUserValidator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ezuykow
 */
@Component
public class OwnerCommandsActions {

    private final ChatAndUserValidator validator;
    private final AdminService adminService;
    private final GlobalChatService globalChatService;
    private final AdminChatService adminChatService;
    private final AdminChatMembersService adminChatMembersService;
    private final MessageSender msgSender;
    private final Environment env;

    public OwnerCommandsActions(ChatAndUserValidator validator, AdminService adminService, GlobalChatService globalChatService,
                                AdminChatService adminChatService, AdminChatMembersService adminChatMembersService,
                                MessageSender msgSender, Environment env)
    {
        this.validator = validator;
        this.adminService = adminService;
        this.globalChatService = globalChatService;
        this.adminChatService = adminChatService;
        this.adminChatMembersService = adminChatMembersService;
        this.msgSender = msgSender;
        this.env = env;
    }

    /**
     * @author ezuykow
     */
    public void validateAndPerformStartCmd(long chatId) {
        if (validator.chatNotAdded(chatId)) {
            performStartCmd(chatId);
        } else {
            msgSender.send(chatId, env.getProperty("messages.owner.validation.startCmdFailed"));
        }
    }

    /**
     * @author ezuykow
     */
    public void validateAndPerformAdminOnCmd(long chatId) {
        if (validator.isGlobalChat(chatId)) {
            performAdminOnCmd(chatId);
        } else {
            msgSender.send(chatId, env.getProperty("messages.owner.validation.adminOnCmdFailed"));
        }
    }


    /**
     * @author ezuykow
     */
    public void validateAndPerformAdminOffCmd(long chatId) {
        if (validator.isAdminChat(chatId)) {
            performAdminOffCmd(chatId);
        } else {
            msgSender.send(chatId, env.getProperty("messages.owner.validation.chatIsNotAdmin"));
        }
    }

    /**
     * @author ezuykow
     */
    public void validateAndPerformPromoteCmd(long chatId) {
        if (validator.isAdminChat(chatId)) {
            validateAdminChatMembersAndPerformPromoteCmd(chatId);
        } else {
            msgSender.send(chatId, env.getProperty("messages.owner.validation.chatIsNotAdmin"));
        }
    }

    /**
     * @author ezuykow
     */
    public void validateAndPerformDemoteCmd(long chatId) {
        if (validator.isAdminChat(chatId)) {
            validateAdminsInChatAndPerformDemoteCmd(chatId);
        } else {
            msgSender.send(chatId, env.getProperty("messages.owner.validation.chatIsNotAdmin"));
        }
    }

    /**
     * @author ezuykow
     */
    private void performStartCmd(long chatId) {
        globalChatService.save(new GlobalChat(chatId));
        msgSender.send(chatId, env.getProperty("messages.welcome"));
    }

    /**
     * @author ezuykow
     */
    private void performAdminOnCmd(long chatId) {
        Admin owner = adminService.getOwner();
        swapGlobalChatToAdminChat(chatId, owner);
        createAdminChatMembers(chatId, owner);
        msgSender.send(chatId, env.getProperty("messages.owner.chatIsAdminNow"));
    }

    /**
     * @author ezuykow
     */
    private void performAdminOffCmd(long chatId) {
        swapAdminChatToGlobalChat(chatId);
        adminChatMembersService.deleteByChatId(chatId);
        adminService.deleteUselessAdmins();
        msgSender.send(chatId, env.getProperty("messages.owner.chatIsGlobalNow"));
    }

    /**
     * @author ezuykow
     */
    private void validateAdminChatMembersAndPerformPromoteCmd(long chatId) {
        Set<User> notAdminMembers = getNotAdminMembersInAdminChat(chatId);
        if (notAdminMembers.isEmpty()) {
            msgSender.send(chatId, env.getProperty("messages.owner.emptyPromotionList"));
        } else {
            msgSender.send(chatId, env.getProperty("messages.owner.promote"),
                    PromoteOrDemoteUserKeyboard.createKeyboard(notAdminMembers, "PromoteUser.")
            );
        }
    }

    /**
     * @author ezuykow
     */
    private void validateAdminsInChatAndPerformDemoteCmd(long chatId) {
        Set<User> admins = getAdminUsersFromChat(chatId);
        if (admins.isEmpty()) {
            msgSender.send(chatId, env.getProperty("messages.owner.emptyPromotionList"));
        } else {
            msgSender.send(chatId, env.getProperty("messages.owner.demote"),
                    PromoteOrDemoteUserKeyboard.createKeyboard(admins, "DemoteUser.")
            );
        }
    }

    /**
     * @author ezuykow
     */
    private void swapGlobalChatToAdminChat(long chatId, Admin owner) {
        globalChatService.deleteById(chatId);
        adminChatService.save(
                new AdminChat(
                        chatId,
                        Collections.singleton(owner)
                )
        );
    }

    /**
     * @author ezuykow
     */
    private void swapAdminChatToGlobalChat(long chatId) {
        adminChatService.deleteByChatId(chatId);
        globalChatService.save(new GlobalChat(chatId));
    }

    /**
     * @author ezuykow
     */
    private void createAdminChatMembers(long chatId, Admin owner) {
        adminChatMembersService.save(
                new AdminChatMembers(
                        chatId,
                        new long[]{owner.getTgAdminUserId()}
                )
        );
    }

    /**
     * @author ezuykow
     */
    private Set<User> getNotAdminMembersInAdminChat(long chatId) {
        Set<Long> membersIds = getAdminChatMembersIds(chatId);
        membersIds.removeAll(getAdminsIdsFromChat(chatId));
        return membersIds.stream().map(id -> msgSender.getChatMember(chatId, id))
                .collect(Collectors.toSet());
    }

    /**
     * @author ezuykow
     */
    private Set<Long> getAdminChatMembersIds(long chatId) {
        return new HashSet<>(
                Set.of(ArrayUtils.toObject(adminChatMembersService.findByChatId(chatId).get().getMembers())));
    }

    /**
     * @author ezuykow
     */
    private Set<User> getAdminUsersFromChat(long chatId) {
        Set<Long> adminsIdsFromChat = getAdminsIdsFromChat(chatId);
        adminsIdsFromChat.remove(adminService.getOwner().getTgAdminUserId());
        return  adminsIdsFromChat.stream()
                .map(id -> msgSender.getChatMember(chatId, id))
                .collect(Collectors.toSet());
    }

    /**
     * @author ezuykow
     */
    private Set<Long> getAdminsIdsFromChat(long chatId) {
        return adminChatService.findById(chatId).get().getAdmins()
                .stream().map(Admin::getTgAdminUserId).collect(Collectors.toSet());
    }
}

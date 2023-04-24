package ru.coffeecoders.questbot.actions;

import com.pengrad.telegrambot.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Admin;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.entities.AdminChatMembers;
import ru.coffeecoders.questbot.exceptions.NonExistentChat;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.services.AdminChatMembersService;
import ru.coffeecoders.questbot.services.AdminChatService;
import ru.coffeecoders.questbot.services.AdminService;
import ru.coffeecoders.questbot.services.GlobalChatService;
import ru.coffeecoders.questbot.validators.ChatAndUserValidator;

import java.util.Arrays;
import java.util.List;

/**
 * @author ezuykow
 */
@Component
public class ChatMembersActions {

    @Value("${messages.members.welcomePrefix}")
    private String welcomePrefix;
    @Value("${messages.members.welcomeSuffix}")
    private String welcomeSuffix;
    @Value("${messages.members.byePrefix}")
    private String byePrefix;
    @Value("${messages.members.byeSuffix}")
    private String byeSuffix;
    @Value("${messages.members.welcomeAdminPrefix}")
    private String welcomeAdminPrefix;
    @Value("${messages.members.welcomeAdminSuffix}")
    private String welcomeAdminSuffix;
    @Value("${messages.members.byeAdmin}")
    private String byeAdmin;
    @Value("${messages.members.ownerLeftChat}")
    private String ownerLeftChat;

    private final AdminChatMembersService adminChatMembersService;
    private final GlobalChatService globalChatService;
    private final AdminChatService adminChatService;
    private final AdminService adminService;
    private final ChatAndUserValidator validator;
    private final MessageSender msgSender;

    public ChatMembersActions(AdminChatMembersService adminChatMembersService, GlobalChatService globalChatService,
                              AdminChatService adminChatService, AdminService adminService,
                              ChatAndUserValidator validator, MessageSender msgSender)
    {
        this.adminChatMembersService = adminChatMembersService;
        this.globalChatService = globalChatService;
        this.adminChatService = adminChatService;
        this.adminService = adminService;
        this.validator = validator;
        this.msgSender = msgSender;
    }

    //-----------------API START-----------------

    /**
     * Если чат админский, то вызывает {@link ChatMembersActions#newChatMemberInAdminChat},
     * Если чат не админский, то вызывает {@link ChatMembersActions#newChatMemberInGlobalChat}
     * @param newMember новый пользователь чата
     * @param chatId id чата
     * @author ezuykow
     */
    public void newChatMember(User newMember, long chatId) {
        if (validator.isAdminChat(chatId)) {
            newChatMemberInAdminChat(newMember, chatId);
        } else if (validator.isGlobalChat(chatId)){
            newChatMemberInGlobalChat(newMember, chatId);
        }
    }

    /**
     * Если чат админский, то вызывает {@link ChatMembersActions#leftChatMemberInAdminChat},
     * Если чат не админский, то вызывает {@link ChatMembersActions#leftChatMemberInGlobalChat}
     * @param leftMember пользователь, покинувший чат
     * @param chatId id чата
     * @author ezuykow
     */
    public void leftChatMember(User leftMember, long chatId) {
        if (validator.isAdminChat(chatId)) {
            leftChatMemberInAdminChat(leftMember, chatId);
        } else if (validator.isGlobalChat(chatId)){
            leftChatMemberInGlobalChat(leftMember, chatId);
        }
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void newChatMemberInAdminChat(User newMember, long chatId) {
        StringBuilder nameSB = new StringBuilder(newMember.firstName());
        if (newMember.lastName() != null) {
                nameSB.append(" ").append(newMember.lastName());
        }
        msgSender.send(chatId, welcomeAdminPrefix + nameSB + welcomeAdminSuffix);
        refreshAdminChatMember(newMember, chatId);
    }

    /**
     * @author ezuykow
     */
    private void newChatMemberInGlobalChat(User newMember, long chatId) {
        StringBuilder nameSB = new StringBuilder(newMember.firstName());
        if (newMember.lastName() != null) {
                nameSB.append(" ").append(newMember.lastName());
        }
        msgSender.send(chatId, welcomePrefix + nameSB + welcomeSuffix);
    }

    /**
     * @author ezuykow
     */
    private void leftChatMemberInAdminChat(User leftMember, long chatId) {
        if (isOwner(leftMember)) {
            ownerLeftAdminChat(chatId);
        } else {
            if (hasItMemberInAdminChatsMembers(chatId, leftMember)) {
                deleteAdminFromChat(chatId, leftMember);
            }
            StringBuilder nameSB = new StringBuilder(leftMember.firstName());
            if (leftMember.lastName() != null) {
                nameSB.append(" ").append(leftMember.lastName());
            }
            msgSender.send(chatId, nameSB + byeAdmin);
        }
    }

    /**
     * @author ezuykow
     */
    private boolean isOwner(User leftMember) {
        return adminService.findById(leftMember.id()).filter(Admin::isOwner).isPresent();
    }

    /**
     * @author ezuykow
     */
    private void leftChatMemberInGlobalChat(User leftMember, long chatId) {
        if (isOwner(leftMember)) {
            ownerLeftGlobalChat(chatId);
        } else {
            StringBuilder nameSB = new StringBuilder(leftMember.firstName());
            if (leftMember.lastName() != null) {
                nameSB.append(" ").append(leftMember.lastName());
            }
            msgSender.send(chatId, byePrefix + nameSB + byeSuffix);
        }
    }

    /**
     * @author ezuykow
     */
    private void refreshAdminChatMember(User newMember, long chatId) {
        AdminChatMembers adminChatMembers = adminChatMembersService.findByChatId(chatId)
                .orElseThrow(NonExistentChat::new);
        adminChatMembers.setMembers(refreshMember(adminChatMembers.getMembers(), newMember));
        adminChatMembersService.save(adminChatMembers);
    }

    /**
     * @author ezuykow
     */
    private long[] refreshMember(long[] oldMembers, User newMember) {
        long[] refreshedMembers = Arrays.copyOf(oldMembers, oldMembers.length + 1);
        refreshedMembers[refreshedMembers.length - 1] = newMember.id();
        return refreshedMembers;
    }

    /**
     * @author ezuykow
     */
    private void ownerLeftGlobalChat(long chatId) {
        globalChatService.deleteById(chatId);
        ownerLeftChat(chatId);
    }

    /**
     * @author ezuykow
     */
    private void ownerLeftAdminChat(long chatId) {
        deleteAdminChat(chatId);
        ownerLeftChat(chatId);
    }

    /**
     * @author ezuykow
     */
    private void ownerLeftChat(long chatId) {
        msgSender.send(chatId, ownerLeftChat);
        msgSender.sendLeaveChat(chatId);
    }

    /**
     * @author ezuykow
     */
    private void deleteAdminChat(long chatId) {
        adminChatService.deleteByChatId(chatId);
        deleteAllUselessAdmins();
        adminChatMembersService.deleteByChatId(chatId);
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
    private void deleteAdminFromChat(long chatId, User leftMember) {
        AdminChat adminChat = adminChatService.findById(chatId).orElseThrow(NonExistentChat::new);
        adminChat.getAdmins().remove(new Admin(leftMember.id()));
        adminChatService.save(adminChat);
        deleteAllUselessAdmins();
        deleteMemberFromAdminChatsMembers(chatId, leftMember);
    }

    /**
     * @author ezuykow
     */
    private void deleteMemberFromAdminChatsMembers(long chatId, User leftMember) {
        AdminChatMembers adminChatMembers = adminChatMembersService.findByChatId(chatId)
                .orElseThrow(NonExistentChat::new);
        long[] newMembers = Arrays.stream(adminChatMembers.getMembers())
                .filter(id -> !(id == leftMember.id())).toArray();
        adminChatMembers.setMembers(newMembers);
        adminChatMembersService.save(adminChatMembers);
    }

    /**
     * @author ezuykow
     */
    private boolean hasItMemberInAdminChatsMembers(long chatId, User leftMember) {
        return Arrays.stream(adminChatMembersService.findByChatId(chatId)
                        .orElseThrow(NonExistentChat::new).getMembers())
                .filter(id -> id == leftMember.id()).findAny().isPresent();
    }
}

package ru.coffeecoders.questbot.actions;

import com.pengrad.telegrambot.model.User;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Admin;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.entities.AdminChatMembers;
import ru.coffeecoders.questbot.exceptions.NonExistentChat;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.services.AdminChatMembersService;
import ru.coffeecoders.questbot.services.AdminChatService;
import ru.coffeecoders.questbot.services.AdminService;
import ru.coffeecoders.questbot.validators.ChatAndUserValidator;

import java.util.Arrays;
import java.util.List;

/**
 * @author ezuykow
 */
@Component
public class ChatMembersActions {

    private final AdminChatMembersService adminChatMembersService;
    private final AdminChatService adminChatService;
    private final AdminService adminService;
    private final ChatAndUserValidator validator;
    private final MessageSender msgSender;
    private final Messages messages;

    public ChatMembersActions(AdminChatMembersService adminChatMembersService, AdminChatService adminChatService,
                              AdminService adminService, ChatAndUserValidator validator, MessageSender msgSender,
                              Messages messages)
    {
        this.adminChatMembersService = adminChatMembersService;
        this.adminChatService = adminChatService;
        this.adminService = adminService;
        this.validator = validator;
        this.msgSender = msgSender;
        this.messages = messages;
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
        msgSender.send(chatId, messages.welcomeAdminPrefix() + nameSB + messages.welcomeAdminSuffix());
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
        msgSender.send(chatId, messages.welcomePrefix() + nameSB + messages.welcomeSuffix());
    }

    /**
     * @author ezuykow
     */
    private void leftChatMemberInAdminChat(User leftMember, long chatId) {
        if (hasItMemberInAdminChatsMembers(chatId, leftMember)) {
            deleteAdminFromChat(chatId, leftMember);
        }
        StringBuilder nameSB = new StringBuilder(leftMember.firstName());
        if (leftMember.lastName() != null) {
            nameSB.append(" ").append(leftMember.lastName());
        }
        msgSender.send(chatId, nameSB + messages.byeAdmin());
    }

    /**
     * @author ezuykow
     */
    private void leftChatMemberInGlobalChat(User leftMember, long chatId) {
        StringBuilder nameSB = new StringBuilder(leftMember.firstName());
        if (leftMember.lastName() != null) {
            nameSB.append(" ").append(leftMember.lastName());
        }
        msgSender.send(chatId, messages.byePrefix() + nameSB + messages.byeSuffix());
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

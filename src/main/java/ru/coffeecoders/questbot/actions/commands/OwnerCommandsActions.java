package ru.coffeecoders.questbot.actions.commands;

import com.pengrad.telegrambot.model.User;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Admin;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.entities.AdminChatMembers;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.exceptions.NonExistentChat;
import ru.coffeecoders.questbot.keyboards.PromoteOrDemoteUserKeyboard;
import ru.coffeecoders.questbot.managers.ApplicationShutdownManager;
import ru.coffeecoders.questbot.managers.BlockingManager;
import ru.coffeecoders.questbot.managers.RestrictingManager;
import ru.coffeecoders.questbot.messages.MessageBuilder;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.properties.viewer.PropertiesViewer;
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
    private final PropertiesViewer propertyViewer;
    private final Messages messages;
    private final MessageBuilder messageBuilder;
    private final MessageSender msgSender;
    private final BlockingManager blockingManager;
    private final RestrictingManager restrictingManager;
    private final ApplicationShutdownManager applicationShutdownManager;

    public OwnerCommandsActions(ChatAndUserValidator validator, AdminService adminService,
                                GlobalChatService globalChatService, AdminChatService adminChatService,
                                AdminChatMembersService adminChatMembersService,
                                PropertiesViewer propertyViewer, Messages messages, MessageBuilder messageBuilder,
                                MessageSender msgSender, BlockingManager blockingManager,
                                RestrictingManager restrictingManager,
                                ApplicationShutdownManager applicationShutdownManager)
    {
        this.validator = validator;
        this.adminService = adminService;
        this.globalChatService = globalChatService;
        this.adminChatService = adminChatService;
        this.adminChatMembersService = adminChatMembersService;
        this.propertyViewer = propertyViewer;
        this.messages = messages;
        this.messageBuilder = messageBuilder;
        this.msgSender = msgSender;
        this.blockingManager = blockingManager;
        this.restrictingManager = restrictingManager;
        this.applicationShutdownManager = applicationShutdownManager;
    }

    //-----------------API START-----------------

    /**
     * Проверяет, что чата с таким chatId нет в системе и вызывает {@link OwnerCommandsActions#performStartCmd},
     * в противном случае отправляет в чат сообщение, что чат уже был добавлен
     * @param chatId id чата, в котором вызвали команду /start
     * @author ezuykow
     */
    public void validateAndPerformStartCmd(long chatId) {
        if (validator.chatNotAdded(chatId)) {
            performStartCmd(chatId);
        } else {
            msgSender.send(chatId,
                    messageBuilder.build(messages.startCmdFailed(), chatId));
        }
    }

    /**
     * Проверяет, что чат с таким chatId не админский и вызывает {@link OwnerCommandsActions#performAdminOnCmd},
     * в противном случае отправляет в чат сообщение, что чат уже админский
     * @param chatId id чата, в котором вызвали команду /adminon
     * @author ezuykow
     */
    public void validateAndPerformAdminOnCmd(long chatId) {
        if (validator.isGlobalChat(chatId)) {
            performAdminOnCmd(chatId);
        } else {
            msgSender.send(chatId,
                    messageBuilder.build(messages.adminOnCmdFailed(), chatId));
        }
    }

    /**
     * Проверяет, что чат с таким chatId админский и вызывает {@link OwnerCommandsActions#performAdminOffCmd},
     * в противном случае отправляет в чат сообщение, что чат не админский
     * @param chatId id чата, в котором вызвали команду /adminoff
     * @author ezuykow
     */
    public void validateAndPerformAdminOffCmd(long chatId) {
        if (validator.isAdminChat(chatId)) {
            performAdminOffCmd(chatId);
        } else {
            msgSender.send(chatId,
                    messageBuilder.build(messages.chatIsNotAdmin(), chatId));
        }
    }

    /**
     * Проверяет, что чат с таким chatId админский и вызывает
     * {@link OwnerCommandsActions#validateAdminChatMembersAndPerformPromoteCmd},
     * в противном случае отправляет в чат сообщение, что чат не админский
     * @param chatId id чата, в котором вызвали команду /promote
     * @author ezuykow
     */
    public void validateAndPerformPromoteCmd(long chatId) {
        if (validator.isAdminChat(chatId)) {
            validateAdminChatMembersAndPerformPromoteCmd(chatId);
        } else {
            msgSender.send(chatId,
                    messageBuilder.build(messages.chatIsNotAdmin(), chatId));
        }
    }

    /**
     * Проверяет, что чат с таким chatId админский и вызывает
     * {@link OwnerCommandsActions#validateAdminsInChatAndPerformDemoteCmd},
     * в противном случае отправляет в чат сообщение, что чат не админский
     * @param chatId id чата, в котором вызвали команду /demote
     * @author ezuykow
     */
    public void validateAndPerformDemoteCmd(long chatId) {
        if (validator.isAdminChat(chatId)) {
            validateAdminsInChatAndPerformDemoteCmd(chatId);
        } else {
            msgSender.send(chatId,
                    messageBuilder.build(messages.chatIsNotAdmin(), chatId));
        }
    }

    /**
     * @author ezuykow
     */
    public void validateAndPerformPropertiesCmd(long chatId) {
        if (validator.isAdminChat(chatId)) {
            blockingManager.blockAdminChatByAdmin(chatId, -1, "Владелец просматривает параметры");
            restrictingManager.restrictMembers(chatId, -1);
            propertyViewer.viewProperties(chatId);
        } else {
            msgSender.send(chatId,
                    messageBuilder.build(messages.chatIsNotAdmin(), chatId));
        }
    }

    /**
     * Вызывает {@link MessageSender#sendStopBot} и {@link ApplicationShutdownManager#stopBot}
     * @author ezuykow
     */
    public void performStopBotCmd() {
        msgSender.sendStopBot();
        applicationShutdownManager.stopBot();
    }

    //-----------------API END-----------------
    
    /**
     * Создает новый {@link GlobalChat}, вызывает для него {@link GlobalChatService#save}
     * и отправляет в чат приветственное сообщение
     * @param chatId id чата, в котором вызвали команду /start
     * @author ezuykow
     */
    private void performStartCmd(long chatId) {
        globalChatService.save(new GlobalChat(chatId));
        msgSender.send(chatId,
                messageBuilder.build(messages.welcome(), chatId));
    }

    /**
     * Вызывает {@link OwnerCommandsActions#swapGlobalChatToAdminChat} и
     * {@link OwnerCommandsActions#createAdminChatMembers},
     * отправляет в чат сообщение, что чат теперь админский
     * @param chatId id чата, в котором вызвали команду /adminon
     * @author ezuykow
     */
    private void performAdminOnCmd(long chatId) {
        Admin owner = adminService.getOwner();
        swapGlobalChatToAdminChat(chatId, owner);
        createAdminChatMembers(chatId, owner);
        msgSender.send(chatId,
                messageBuilder.build(messages.chatIsAdminNow(), chatId));
    }

    /**
     * Вызывает {@link OwnerCommandsActions#swapAdminChatToGlobalChat},
     * {@link AdminChatMembersService#deleteByChatId},
     * {@link AdminService#deleteUselessAdmins},
     * отправляет в чат сообщение, что чат теперь не админский
     * @param chatId id чата, в котором вызвали команду /adminoff
     * @author ezuykow
     */
    private void performAdminOffCmd(long chatId) {
        swapAdminChatToGlobalChat(chatId);
        adminChatMembersService.deleteByChatId(chatId);
        adminService.deleteUselessAdmins();
        msgSender.send(chatId,
                messageBuilder.build(messages.chatIsGlobalNow(), chatId));
    }

    /**
     * Проверяет, что в чате есть члены не админы и отправляет сообщение для выбора повышаемого члена с
     * клавиатурой {@link PromoteOrDemoteUserKeyboard}, в противном случае
     * отправляет в чат сообщение, что в чате нет не админов
     * @param chatId id чата, в котором вызвали команду /promote
     * @author ezuykow
     */
    private void validateAdminChatMembersAndPerformPromoteCmd(long chatId) {
        Set<User> notAdminMembers = getNotAdminMembersInAdminChat(chatId);
        if (notAdminMembers.isEmpty()) {
            msgSender.send(chatId,
                    messageBuilder.build(messages.emptyPromotionList(), chatId));
        } else {
            msgSender.send(chatId,
                    messageBuilder.build(messages.promote(), chatId),
                    PromoteOrDemoteUserKeyboard.createKeyboard(notAdminMembers, "PromoteUser.")
            );
        }
    }

    /**
     * Проверяет, что в чате есть админы и отправляет сообщение для выбора понижаемого члена с
     * клавиатурой {@link PromoteOrDemoteUserKeyboard}, в противном случае
     * отправляет в чат сообщение, что в чате нет админов
     * @param chatId id чата, в котором вызвали команду /demote
     * @author ezuykow
     */
    private void validateAdminsInChatAndPerformDemoteCmd(long chatId) {
        Set<User> admins = getAdminUsersFromChat(chatId);
        if (admins.isEmpty()) {
            msgSender.send(chatId,
                    messageBuilder.build(messages.emptyPromotionList(), chatId));
        } else {
            msgSender.send(chatId,
                    messageBuilder.build(messages.demote(), chatId),
                    PromoteOrDemoteUserKeyboard.createKeyboard(admins, "DemoteUser.")
            );
        }
    }

    /**
     * Вызывает {@link GlobalChatService#deleteById} для {@code chatId}, создает новый
     * {@link AdminChat} c {@code chatId} и админом {@code owner} и вызывает для него {@link AdminChatService#save}
     * @param chatId id чата
     * @param owner админ-владелец бота
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
     * Вызывает {@link AdminChatService#deleteByChatId} для {@code chatId}, создает новый
     * {@link GlobalChat} c {@code chatId} и вызывает для него {@link GlobalChatService#save}
     * @param chatId id чата
     * @author ezuykow
     */
    private void swapAdminChatToGlobalChat(long chatId) {
        adminChatService.deleteByChatId(chatId);
        globalChatService.save(new GlobalChat(chatId));
    }

    /**
     * Создает {@link AdminChatMembers} с {@code chatId} и {@code owner.getTgAdminUserId()}
     * и вызывает для него {@link AdminChatMembersService#save}
     * @param chatId id чата
     * @param owner админ-владелец бота
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
     * Получает список id всех членов чата, убирает из него id админов, получает {@link User} по каждому id
     * и возвращает {@code Set<User>}
     * @param chatId id чата
     * @return {@code Set<User>} - сет членов чата, которые не являются админами
     * @author ezuykow
     */
    private Set<User> getNotAdminMembersInAdminChat(long chatId) {
        Set<Long> membersIds = getAdminChatMembersIds(chatId);
        membersIds.removeAll(getAdminsIdsFromChat(chatId));
        return membersIds.stream().map(id -> msgSender.getChatMember(chatId, id))
                .collect(Collectors.toSet());
    }

    /**
     * Получает членов чата по {@code chatId} как {@code long[] ids}, преобразует в {@code Set<Long>} и возвращает
     * @param chatId id чата
     * @return {@code Set<Long>} - сет из id членов чата
     * @author ezuykow
     */
    private Set<Long> getAdminChatMembersIds(long chatId) {
        return new HashSet<>(
                Set.of(ArrayUtils.toObject(adminChatMembersService.findByChatId(chatId)
                                .orElseThrow(NonExistentChat::new).getMembers())));
    }

    /**
     * Получает список id админов чата, убирает из него id хозяина бота, получает {@link User} по каждому id
     * и возвращает {@code Set<User>}
     * @param chatId id чата
     * @return {@code Set<User>} - сет членов чата, которые являются админами
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
     * Получает список id админов чата по {@code chatId}, преобразует в {@code Set<Long>} и возвращает
     * @param chatId id чата
     * @return {@code Set<Long>} - сет из id админов чата
     * @author ezuykow
     */
    private Set<Long> getAdminsIdsFromChat(long chatId) {
        return adminChatService.findById(chatId).orElseThrow(NonExistentChat::new).getAdmins()
                .stream().map(Admin::getTgAdminUserId).collect(Collectors.toSet());
    }
}

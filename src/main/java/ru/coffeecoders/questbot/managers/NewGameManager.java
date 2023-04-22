package ru.coffeecoders.questbot.managers;

import com.pengrad.telegrambot.model.ChatPermissions;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.actions.NewGameActions;
import ru.coffeecoders.questbot.entities.NewGameCreatingState;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.services.AdminChatMembersService;
import ru.coffeecoders.questbot.services.AdminService;
import ru.coffeecoders.questbot.utils.BlockAdminChat;

import java.util.Arrays;

/**
 * @author ezuykow
 */
@Component
public class NewGameManager {

    private enum NewGamePartType {
        GAME_NAME,
        START_COUNT_TASKS,
        MAX_QUESTIONS_COUNT,
        MAX_PERFORMED_QUESTIONS_COUNT,
        MIN_QUESTIONS_COUNT_IN_GAME,
        QUESTIONS_COUNT_TO_ADD,
        MAX_TIME_MINUTES
    }

    private final NewGameActions actions;
    private final BlockAdminChat blockAdminChat;
    private final AdminChatMembersService adminChatMembersService;
    private final AdminService adminService;
    private final MessageSender msgSender;

    public NewGameManager(NewGameActions actions, BlockAdminChat blockAdminChat, AdminChatMembersService adminChatMembersService, AdminService adminService, MessageSender msgSender) {
        this.actions = actions;
        this.blockAdminChat = blockAdminChat;
        this.adminChatMembersService = adminChatMembersService;
        this.adminService = adminService;
        this.msgSender = msgSender;
    }

    public void startCreatingGame(long senderAdminId, long chatId) {
        blockAdminChat.validateAndBlockAdminChatByAdmin(chatId, senderAdminId);
        restrictOtherChatMembers(senderAdminId, chatId);
        actions.createNewGameCreatingState(chatId);
    }

    public void manageNewGamePart(long chatId, String text, int msgId) {
        NewGameCreatingState state = actions.getNewGameCreatingState(chatId);
        switch (getExpectedNewGamePartType(state)) {
            case GAME_NAME ->
                    actions.addGameNameToStateAndRequestNextPart(chatId, state, text, msgId);
            case MAX_QUESTIONS_COUNT ->
                    actions.addMaxQuestionsCountToStateAndRequestNextPart(chatId, state, text, msgId);
            case START_COUNT_TASKS ->
                    actions.addStartCountTaskToStateAndRequestNextPart(chatId, state, text, msgId);
            case MAX_PERFORMED_QUESTIONS_COUNT ->
                    actions.addMaxPerformedQuestionsCountToStateAndRequestNextPart(chatId, state, text, msgId);
            case MIN_QUESTIONS_COUNT_IN_GAME ->
                actions.addMinQuestionsCountInGameAndRequestNextPart(chatId, state, text, msgId);
            case QUESTIONS_COUNT_TO_ADD ->
                actions.addQuestionsCountToAddAndRequestNextPart(chatId, state, text, msgId);
            case MAX_TIME_MINUTES ->
                actions.addMaxTimeMinutesToStateAmdSaveNewGame(chatId, state, text, msgId);
        }
    }

    private NewGamePartType getExpectedNewGamePartType(NewGameCreatingState state) {
        if (state.getGameName() == null) {
            return NewGamePartType.GAME_NAME;
        }
        if (state.getMaxQuestionsCount() == null) {
            return NewGamePartType.MAX_QUESTIONS_COUNT;
        }
        if (state.getStartCountTasks() == null) {
            return NewGamePartType.START_COUNT_TASKS;
        }
        if (state.getMaxPerformedQuestionsCount() == null) {
            return NewGamePartType.MAX_PERFORMED_QUESTIONS_COUNT;
        }
        if (state.getMinQuestionsCountInGame() == null) {
            return NewGamePartType.MIN_QUESTIONS_COUNT_IN_GAME;
        }
        if (state.getQuestionsCountToAdd() == null) {
            return NewGamePartType.QUESTIONS_COUNT_TO_ADD;
        }
        return NewGamePartType.MAX_TIME_MINUTES;
    }

    private void restrictOtherChatMembers(long senderAdminId, long chatId) {
        ChatPermissions permissions = new ChatPermissions()
                .canSendMessages(false)
                .canSendOtherMessages(false);

        Arrays.stream(adminChatMembersService.findByChatId(chatId).get().getMembers())
                .filter(id -> (id != senderAdminId) && (id != adminService.getOwner().getTgAdminUserId()))
                .forEach(id -> msgSender.sendRestrictChatMember(chatId, id, permissions));
    }
}

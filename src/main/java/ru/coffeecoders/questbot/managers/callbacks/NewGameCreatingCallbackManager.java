package ru.coffeecoders.questbot.managers.callbacks;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.actions.newgame.NewGameActions;
import ru.coffeecoders.questbot.services.AdminChatService;
import ru.coffeecoders.questbot.validators.ChatAndUserValidator;

/**
 * @author ezuykow
 */
@Component
public class NewGameCreatingCallbackManager {

    private final AdminChatService adminChatService;
    private final NewGameActions newGameActions;
    private final ChatAndUserValidator validator;

    public NewGameCreatingCallbackManager(AdminChatService adminChatService, NewGameActions newGameActions, ChatAndUserValidator validator) {
        this.adminChatService = adminChatService;
        this.newGameActions = newGameActions;
        this.validator = validator;
    }

    public void manageCallback(long senderUserId, long chatId, int msgId, String data) {
        long blockedAdminId = adminChatService.findById(chatId).get().getBlockedByAdminId();
        if ((senderUserId == blockedAdminId) || validator.isOwner(senderUserId)) {
            performCallback(chatId, msgId, data);
        }
    }

    private void performCallback(long chatId, int msgId, String data) {
        String dataPart = data.substring(data.indexOf(".") + 1);
        if (dataPart.equals("Stop")) {
            newGameActions.stopSelectingQuestionsGroupsAndRequestNextPart(chatId, msgId);
        } else {
            int questionGroupId = Integer.parseInt(dataPart);
            newGameActions.addSelectedQuestionGroupAndRefreshMsg(chatId, msgId, questionGroupId);
        }
    }
}

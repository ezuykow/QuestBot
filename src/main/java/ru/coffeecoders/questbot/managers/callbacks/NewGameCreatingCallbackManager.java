package ru.coffeecoders.questbot.managers.callbacks;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.actions.newgame.NewGameActions;
import ru.coffeecoders.questbot.validators.ChatAndUserValidator;

/**
 * @author ezuykow
 */
@Component
public class NewGameCreatingCallbackManager {

    private final NewGameActions newGameActions;
    private final ChatAndUserValidator validator;

    public NewGameCreatingCallbackManager(NewGameActions newGameActions, ChatAndUserValidator validator) {
        this.newGameActions = newGameActions;
        this.validator = validator;
    }

    //-----------------API START-----------------

    /**
     * Проверяет, что {@code senderUserId} это id админа, который заблокировал чат или владелец бота,
     * вызывает {@link NewGameCreatingCallbackManager#performCallback}
     * @param senderUserId id пользователя, от которого пришел калбак
     * @param chatId id чата
     * @param msgId id сообщения
     * @param data данные калбака
     * @author ezuykow
     */
    public void manageCallback(long senderUserId, long chatId, int msgId, String data) {
        if (validator.isBlockedAdmin(chatId, senderUserId) || validator.isOwner(senderUserId)) {
            performCallback(chatId, msgId, data);
        }
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
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

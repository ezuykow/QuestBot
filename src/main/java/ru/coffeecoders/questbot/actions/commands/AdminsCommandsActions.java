package ru.coffeecoders.questbot.actions.commands;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.managers.ApplicationShutdownManager;
import ru.coffeecoders.questbot.managers.BlockingManager;
import ru.coffeecoders.questbot.managers.RestrictingManager;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.services.GlobalChatService;
import ru.coffeecoders.questbot.validators.ChatAndUserValidator;
import ru.coffeecoders.questbot.viewers.GamesViewer;
import ru.coffeecoders.questbot.viewers.PrepareGameViewer;
import ru.coffeecoders.questbot.viewers.QuestionsViewer;

@Component
public class AdminsCommandsActions {

    private final GamesViewer gamesViewer;
    private final QuestionsViewer questionsViewer;
    private final BlockingManager blockingManager;
    private final RestrictingManager restrictingManager;
    private final ApplicationShutdownManager applicationShutdownManager;
    private final GlobalChatService globalChatService;
    private final PrepareGameViewer prepareGameViewer;
    private final ChatAndUserValidator validator;
    private final MessageSender msgSender;
    private final Messages messages;

    private AdminsCommandsActions(GamesViewer gamesViewer, MessageSender msgSender, QuestionsViewer questionsViewer,
                                  ApplicationShutdownManager applicationShutdownManager,
                                  BlockingManager blockingManager, RestrictingManager restrictingManager,
                                  GlobalChatService globalChatService, PrepareGameViewer prepareGameViewer, ChatAndUserValidator validator, Messages messages)
    {
        this.gamesViewer = gamesViewer;
        this.msgSender = msgSender;
        this.questionsViewer = questionsViewer;
        this.applicationShutdownManager = applicationShutdownManager;
        this.blockingManager = blockingManager;
        this.restrictingManager = restrictingManager;
        this.globalChatService = globalChatService;
        this.prepareGameViewer = prepareGameViewer;
        this.validator = validator;
        this.messages = messages;
    }

    //-----------------API START-----------------

    /**
     * Блокирует чат, ограничивает членов и вызывает {@link GamesViewer#viewGames}
     * @param senderAdminId id админа, который ввел команду /showgames
     * @param chatId id чата, в котором была введена команда
     * @author ezuykow
     */
    public void performShowGamesCmd(long senderAdminId, long chatId) {
        blockAndRestrictChat(chatId, senderAdminId, messages.startGamesView());
        gamesViewer.viewGames(chatId);
    }

    /**
     * Блокирует чат, ограничивает членов и вызывает {@link QuestionsViewer#viewQuestions}
     * @param senderAdminId id админа, который ввел команду /showquestion
     * @param chatId id чата, в котором была введена команда
     * @author ezuykow
     */
    public void performShowQuestionsCmd(long senderAdminId, long chatId) {
        blockAndRestrictChat(chatId, senderAdminId, messages.startQuestionView());
        questionsViewer.viewQuestions(chatId);
    }

    /**
     * Если этот чат глобальный, то удаляет его из БД, иначе предупреждает, что нельзя удалить
     * @param chatId id чата
     * @author ezuykow
     */
    public void performDeleteChatCmd(long chatId) {
        if (validator.isGlobalChat(chatId)) {
            globalChatService.deleteById(chatId);
            msgSender.send(chatId, messages.chatNotInGame());
            msgSender.sendLeaveChat(chatId);
        } else {
            msgSender.send(chatId, messages.cmdForGlobalChat());
        }
    }

    /**
     * Вызывает {@link PrepareGameViewer#requestGameName} с теми же параметрами, если чат не админский,
     * иначе - сообщение
     * @param senderAdminId id админа, вызвавшего команду
     * @param chatId id чата
     * @author ezuykow
     */
    public void performPrepareGameCmd(long senderAdminId, long chatId) {
        final String adminUsername = "@" + msgSender.getChatMember(chatId, senderAdminId).username();
        if (validator.isGlobalChat(chatId)) {
            prepareGameViewer.requestGameName(senderAdminId, adminUsername, chatId);
        } else {
            msgSender.send(chatId, messages.cmdForGlobalChat() + ", " + adminUsername);
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
     * @author ezuykow
     */
    private void blockAndRestrictChat(long chatId, long senderAdminId, String cause) {
        blockingManager.blockAdminChatByAdmin(chatId, senderAdminId, cause);
        restrictingManager.restrictMembers(chatId, senderAdminId);

    }
}

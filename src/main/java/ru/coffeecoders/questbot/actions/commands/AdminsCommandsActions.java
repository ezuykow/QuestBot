package ru.coffeecoders.questbot.actions.commands;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.services.AdminChatService;
import ru.coffeecoders.questbot.services.GlobalChatService;
import ru.coffeecoders.questbot.viewers.QuestionsViewer;

@Component
public class AdminsCommandsActions {

    private final MessageSender msgSender;
    private final QuestionsViewer questionsViewer;
    private final AdminChatService adminChatService;
    private final GlobalChatService globalChatService;
    private final Environment env;

    private AdminsCommandsActions(MessageSender msgSender, QuestionsViewer questionsViewer, AdminChatService adminChatService, GlobalChatService globalChatService, Environment env) {
        this.msgSender = msgSender;
        this.questionsViewer = questionsViewer;
        this.adminChatService = adminChatService;
        this.globalChatService = globalChatService;
        this.env = env;
    }

    public void performStartCmd(ExtendedUpdate update) {
        GlobalChat globalChat = new GlobalChat();
        globalChat.setTgChatId(update.getMessageChatId());
        globalChatService.save(globalChat);
        msgSender.send(update.getMessageChatId(), env.getProperty("messages.welcome"));
    }

    public void performShowQuestionsCmd(ExtendedUpdate update) {
        questionsViewer.viewQuestions(update.getMessageChatId());
    }

    public void performAdminOnCmd(ExtendedUpdate update) {
        final long chatId = update.getMessageChatId();
        final AdminChat adminChat = new AdminChat();
        adminChat.setTgAdminChatId(chatId);
        adminChatService.save(adminChat);
        msgSender.send(chatId, env.getProperty("messages.admins.chatIsAdminNow"));
    }
}

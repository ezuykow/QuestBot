package ru.coffeecoders.questbot.actions.commands;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.managers.ApplicationShutdownManager;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.viewers.QuestionsViewer;

@Component
public class AdminsCommandsActions {

    private final MessageSender msgSender;
    private final QuestionsViewer questionsViewer;
    private final ApplicationShutdownManager applicationShutdownManager;

    private AdminsCommandsActions(MessageSender msgSender, QuestionsViewer questionsViewer,
                                  ApplicationShutdownManager applicationShutdownManager)
    {
        this.msgSender = msgSender;
        this.questionsViewer = questionsViewer;
        this.applicationShutdownManager = applicationShutdownManager;
    }

    /**
     * @author ezuykow
     */
    public void performShowQuestionsCmd(ExtendedUpdate update) {
        questionsViewer.viewQuestions(update.getMessageChatId());
    }

    /**
     * @author ezuykow
     */
    public void performStopBotCmd() {
        msgSender.sendStopBot();
        applicationShutdownManager.stopBot();
    }
}

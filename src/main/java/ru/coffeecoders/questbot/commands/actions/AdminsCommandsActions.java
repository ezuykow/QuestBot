package ru.coffeecoders.questbot.commands.actions;

import com.pengrad.telegrambot.model.Update;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.viewers.questions.QuestionsViewer;

@Component
public class AdminsCommandsActions {

    private final MessageSender msgSender;
    private final QuestionsViewer questionsViewer;
    private final Environment env;

    private AdminsCommandsActions(MessageSender msgSender, QuestionsViewer questionsViewer, Environment env) {
        this.msgSender = msgSender;
        this.questionsViewer = questionsViewer;
        this.env = env;
    }

    public void performStartCmd(ExtendedUpdate update) {
        msgSender.send(update.getMessageChatId(), env.getProperty("messages.welcome"));
    }

    public void performShowQuestionsCmd(ExtendedUpdate update) {
        questionsViewer.viewQuestions(update.getMessageChatId());
    }
}

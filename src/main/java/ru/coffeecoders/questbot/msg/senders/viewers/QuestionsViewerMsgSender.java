package ru.coffeecoders.questbot.msg.senders.viewers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.viewers.questions.QuestionsViewerPage;

/**
 * @author ezuykow
 */
@Component
public class QuestionsViewerMsgSender {

    private final Logger logger = LoggerFactory.getLogger(QuestionsViewerMsgSender.class);

    private final TelegramBot bot;

    public QuestionsViewerMsgSender(TelegramBot bot) {
        this.bot = bot;
    }

    public void showQuestions(long chatId, QuestionsViewerPage page) {
        SendMessage msg = new SendMessage(chatId, page.getText())
                .replyMarkup(page.getKeyboard());
        send(msg);
    }

    private void send(SendMessage msg) {
        SendResponse response = bot.execute(msg);
        if (!response.isOk()) {
            logger.warn("Unsent msg! {}", response.message().text());
        }
    }
}

package ru.coffeecoders.questbot.viewers;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.models.ExtendedUpdate;
import ru.coffeecoders.questbot.models.QuestionInfoPage;
import ru.coffeecoders.questbot.senders.MessageSender;

/**
 * @author ezuykow
 */
@Component
public class QuestionInfoViewer {

    private final MessageSender msgSender;

    public QuestionInfoViewer(MessageSender msgSender) {
        this.msgSender = msgSender;
    }

    /**
     * Собирает "страницу" отображения вопроса {@link QuestionInfoPage} и отправляет
     * в метод {@link MessageSender#edit} для отображения вопроса
     * @param update апдейт с CallbackQuery
     * @param question вопрос, который необходимо отобразить
     */
    public void showQuestionInfo(ExtendedUpdate update, Question question) {
        QuestionInfoPage page = QuestionInfoPage.createPage(question);
        msgSender.edit(update.getCallbackMessageChatId(), update.getCallbackMessageId(),
                page.getText(), page.getKeyboard());
    }
}

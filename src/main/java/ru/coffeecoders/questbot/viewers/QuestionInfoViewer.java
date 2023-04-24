package ru.coffeecoders.questbot.viewers;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Question;
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

    //-----------------API START-----------------

    /**
     * Собирает "страницу" отображения вопроса {@link QuestionInfoPage} и отправляет
     * в метод {@link MessageSender#edit} для отображения вопроса
     * @param question вопрос, который необходимо отобразить
     * @author ezuykow
     */
    public void showQuestionInfo(long chatId, int msgId, Question question) {
        QuestionInfoPage page = QuestionInfoPage.createPage(question);
        msgSender.edit(chatId, msgId, page.getText(), page.getKeyboard());
    }

    //-----------------API END-----------------

}

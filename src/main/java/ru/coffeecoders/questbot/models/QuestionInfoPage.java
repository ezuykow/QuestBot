package ru.coffeecoders.questbot.models;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.keyboards.viewers.QuestionInfoKeyboard;

import java.util.Date;

/**
 * @author ezuykow
 */
public class QuestionInfoPage {

    private Question question;
    private String text;
    private InlineKeyboardMarkup keyboard;

    private QuestionInfoPage() {}

    //-----------------API START-----------------

    /**
     * Собирает "страницу" отображения вопроса {@code question}, включающую непосредственно текст
     * сообщения {@link QuestionInfoPage#text} и Inline-клавиатуру {@link QuestionInfoPage#keyboard}
     * @param question вопрос, который нужно отобразить
     * @return собранную страницу {@link QuestionInfoPage}
     * @author ezuykow
     */
    public static QuestionInfoPage createPage(Question question) {
        QuestionInfoPage page = new QuestionInfoPage();
        page.question = question;

        page.createText();
        page.createKeyboard();

        return page;
    }

    /**
     * @return {@code QuestionInfoPage.text} - текст страницы
     * @author ezuykow
     */
    public String getText() {
        return text;
    }

    /**
     * @return {@code QuestionInfoPage.keyboard} - клавиатуру страницы
     * @author ezuykow
     */
    public InlineKeyboardMarkup getKeyboard() {
        return keyboard;
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void createText() {
        text = Character.toString(0x2753) + " Вопрос: " + question.getQuestion() + "\n" +
                Character.toString(0x270F) + " Формат ответа: " + answerFormat() + "\n" +
                Character.toString(0x2757) + " Ответ: " + question.getAnswer() + "\n" +
                Character.toString(0X1F5FA) + " Карта: " + mapUrl() + "\n" +
                Character.toString(0x23F1) + " Дата последнего использования: " + lastUsage() + "\n" +
                Character.toString(0x1F4A0) + " Группа: " + group();
    }

    /**
     * @author ezuykow
     */
    private void createKeyboard() {
        keyboard = QuestionInfoKeyboard.createKeyboard(question.getQuestionId());
    }

    /**
     * @author ezuykow
     */
    private String answerFormat() {
        String s = question.getAnswerFormat();
        return (s == null) ? "<пусто>" : s;
    }

    /**
     * @author ezuykow
     */
    private String mapUrl() {
        String s = question.getMapUrl();
        return (s == null) ? "<пусто>" : s;
    }

    /**
     * @author ezuykow
     */
    private String lastUsage() {
        Date date = question.getLastUsage();
        return (date == null) ? "<пусто>" : date.toString();
    }

    /**
     * @author ezuykow
     */
    private String group() {
        String s = question.getGroup();
        return (s == null) ? "<пусто>" : s;
    }
}

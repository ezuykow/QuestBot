package ru.coffeecoders.questbot.models;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.keyboards.viewers.QuestionInfoKeyboardCreator;

import java.util.Date;

/**
 * @author ezuykow
 */
public class QuestionInfoPage {

    private Question question;
    private String text;
    private InlineKeyboardMarkup keyboard;

    private QuestionInfoPage() {}

    /**
     * Собирает "страницу" отображения вопроса {@code question}, включающую непосредственно текст
     * сообщения {@link QuestionInfoPage#text} и Inline-клавиатуру {@link QuestionInfoPage#keyboard}
     * @param question вопрос, который нужно отобразить
     * @return собранную страницу {@link QuestionInfoPage}
     */
    public static QuestionInfoPage createPage(Question question) {
        QuestionInfoPage page = new QuestionInfoPage();
        page.question = question;

        page.createText();
        page.createKeyboard();

        return page;
    }

    public String getText() {
        return text;
    }

    public InlineKeyboardMarkup getKeyboard() {
        return keyboard;
    }

    private void createText() {
        text = Character.toString(0x2753) + " Вопрос: " + question.getQuestion() + "\n" +
                Character.toString(0x270F) + " Формат ответа: " + answerFormat() + "\n" +
                Character.toString(0x2757) + " Ответ: " + question.getAnswer() + "\n" +
                Character.toString(0X1F5FA) + " Карта: " + mapUrl() + "\n" +
                Character.toString(0x23F1) + " Дата последнего использования: " + lastUsage() + "\n" +
                Character.toString(0x1F4A0) + " Группа: " + group();
    }

    private void createKeyboard() {
        keyboard = QuestionInfoKeyboardCreator.createKeyboard(question.getQuestionId());
    }

    private String answerFormat() {
        String s = question.getAnswerFormat();
        return (s == null) ? "<пусто>" : s;
    }

    private String mapUrl() {
        String s = question.getMapUrl();
        return (s == null) ? "<пусто>" : s;
    }

    private String lastUsage() {
        Date date = question.getLastUsage();
        return (date == null) ? "<пусто>" : date.toString();
    }

    private String group() {
        String s = question.getGroup();
        return (s == null) ? "<пусто>" : s;
    }
}

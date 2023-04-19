package ru.coffeecoders.questbot.models;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.beans.factory.annotation.Value;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.keyboards.viewers.QuestionsViewerKeyboard;

import java.util.List;

/**
 * @author ezuykow
 */
public class QuestionsViewerPage {

    private List<Question> questions;
    private int defaultPageSize;
    private int pageSize;
    private int pagesCount;
    private int startIndex;
    private int lastIndex;
    private String text;
    private InlineKeyboardMarkup keyboard;
    private boolean leftArrowNeed;
    private boolean rightArrowNeed;

    private QuestionsViewerPage() {}

    /**
     * Собирает "страницу" отображения всех вопросов {@code questions}, включающую непосредственно текст
     * сообщения {@link QuestionsViewerPage#text} и Inline-клавиатуру {@link QuestionsViewerPage#keyboard}.
     * Собирается "постранично", количество вопросов на "странице" - {@code pageSize}, начиная с вопроса
     * {@code startIndex}
     * @param questions вопросы, который нужно отобразить
     * @param defaultPageSize дефолтное количество вопросов на "странице"
     * @param startIndex индекс вопроса из {@code questions}, который будет первым на "странице"
     * @return собранную страницу {@link QuestionsViewerPage}
     */
    public static QuestionsViewerPage createPage(List<Question> questions, int defaultPageSize, int startIndex, int pagesCount) {
        QuestionsViewerPage page = new QuestionsViewerPage();
        page.questions = questions;
        page.defaultPageSize = defaultPageSize;
        page.startIndex = startIndex;
        page.pageSize = Math.min(defaultPageSize, questions.size());
        page.lastIndex = Math.min(startIndex + page.pageSize - 1, questions.size() - 1);
        page.pagesCount = pagesCount;

        page.createText();
        page.checkArrowsNeed();
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
        StringBuilder sb = new StringBuilder();
        sb.append(calcPage());
        for (int i = startIndex; i <= lastIndex; i++) {
            sb.append(i + 1)
                    .append(". ")
                    .append(questions.get(i).getQuestion())
                    .append("\n");
        }
        text = sb.toString();
    }

    private void checkArrowsNeed() {
        leftArrowNeed = startIndex != 0;
        rightArrowNeed = lastIndex != questions.size() - 1;
    }

    private void createKeyboard() {
        keyboard = QuestionsViewerKeyboard.createKeyboard(
                pageSize, leftArrowNeed, startIndex, lastIndex, rightArrowNeed);
    }

    private String calcPage() {
        int currentPage = startIndex / defaultPageSize + 1;
        return "Страница " + currentPage + " из " + pagesCount + "\n\n";
    }
}

package ru.coffeecoders.questbot.models;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.keyboards.viewers.QuestionsViewerKeyboard;

import java.util.List;

/**
 * @author ezuykow
 */
public class QuestionsViewerPage {

    private final List<Question> questions;
    private final int defaultPageSize;
    private final int pageSize;
    private final int pagesCount;
    private final int startIndex;
    private final int lastIndex;
    private String text;
    private InlineKeyboardMarkup keyboard;
    private boolean leftArrowNeed;
    private boolean rightArrowNeed;

    private QuestionsViewerPage(List<Question> questions, int pageSize, int startIndex,
                                int pagesCount, int defaultPageSize) {
        this.questions = questions;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
        this.pagesCount = pagesCount;
        this.defaultPageSize = defaultPageSize;

        lastIndex = Math.min(startIndex + this.pageSize - 1, questions.size() - 1);
        createText();
        checkArrowsNeed();
        createKeyboard();
    }

    //-----------------API START-----------------

    /**
     * Собирает "страницу" отображения всех вопросов {@code questions}, включающую непосредственно текст
     * сообщения {@link QuestionsViewerPage#text} и Inline-клавиатуру {@link QuestionsViewerPage#keyboard}.
     * Собирается "постранично", количество вопросов на "странице" - {@code pageSize}, начиная с вопроса
     * {@code startIndex}
     * @param questions вопросы, который нужно отобразить
     * @param pageSize дефолтное количество вопросов на "странице"
     * @param startIndex индекс вопроса из {@code questions}, который будет первым на "странице"
     * @return собранную страницу {@link QuestionsViewerPage}
     * @author ezuykow
     */
    public static QuestionsViewerPage createPage(List<Question> questions, int pageSize, int startIndex,
                                                 int pagesCount, int defaultPageSize)
    {
        return new QuestionsViewerPage(questions, pageSize, startIndex, pagesCount, defaultPageSize);
    }

    /**
     * @return {@code QuestionsViewerPage.text} - текст страницы
     * @author ezuykow
     */
    public String getText() {
        return text;
    }

    /**
     * @return {@code QuestionsViewerPage.keyboard} - клавиатуру страницы
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

    /**
     * @author ezuykow
     */
    private void checkArrowsNeed() {
        leftArrowNeed = startIndex != 0;
        rightArrowNeed = lastIndex != questions.size() - 1;
    }

    /**
     * @author ezuykow
     */
    private void createKeyboard() {
        keyboard = QuestionsViewerKeyboard.createKeyboard(
                pageSize, leftArrowNeed, startIndex, lastIndex, rightArrowNeed);
    }

    /**
     * @author ezuykow
     */
    private String calcPage() {
        int currentPage = (startIndex / defaultPageSize) + 1;
        return "Страница " + currentPage + " из " + pagesCount + "\n\n";
    }
}

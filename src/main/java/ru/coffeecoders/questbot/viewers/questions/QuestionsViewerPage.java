package ru.coffeecoders.questbot.viewers.questions;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import ru.coffeecoders.questbot.entities.Question;
import ru.coffeecoders.questbot.keyboards.viewers.QuestionViewerKeyboardCreator;

import java.util.List;

/**
 * @author ezuykow
 */
public class QuestionsViewerPage {

    private List<Question> questions;
    private int pageSize;
    private int startIndex;
    private int lastIndex;
    private String text;
    private InlineKeyboardMarkup keyboard;
    private boolean leftArrowNeed;
    private boolean rightArrowNeed;

    private QuestionsViewerPage() {}

    public static QuestionsViewerPage createPage(List<Question> questions, int pageSize, int startIndex) {
        QuestionsViewerPage page = new QuestionsViewerPage();
        page.questions = questions;
        page.pageSize = pageSize;
        page.startIndex = startIndex;
        page.lastIndex = Math.min(startIndex + pageSize - 1, questions.size() - 1);

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
        keyboard = QuestionViewerKeyboardCreator.createKeyboard(
                pageSize, leftArrowNeed, startIndex, lastIndex, rightArrowNeed);
    }
}

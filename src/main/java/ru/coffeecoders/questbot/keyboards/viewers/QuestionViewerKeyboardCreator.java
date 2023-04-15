package ru.coffeecoders.questbot.keyboards.viewers;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import ru.coffeecoders.questbot.viewers.QuestionsViewer;

import java.util.Arrays;

/**
 * @author ezuykow
 */
public class QuestionViewerKeyboardCreator {

    private InlineKeyboardButton[] buttons;
    private final InlineKeyboardMarkup keyboard;

    private QuestionViewerKeyboardCreator(
            int pageSize, boolean leftArrowNeed, int startIndex, int lastIndex, boolean rightArrowNeed) {

        buttons = new InlineKeyboardButton[pageSize + 2];
        createFirstButton(leftArrowNeed, startIndex);
        createQuestionsButtons(startIndex, lastIndex);
        createLastButton(leftArrowNeed, rightArrowNeed, lastIndex);
        keyboard = new InlineKeyboardMarkup(buttons);
    }

    /**
     * Создает Inline-клавиатуру для {@link QuestionsViewer}
     * @param pageSize количество вопросов на "странице"
     * @param leftArrowNeed нужна ли кнопка "предыдущая страница"
     * @param startIndex индекс первого вопроса на "странице"
     * @param lastIndex индекс последнего вопроса на "странице"
     * @param rightArrowNeed нужна ли кнопка "следующая страница"
     * @return собранный InlineKeyboardMarkup
     * @see InlineKeyboardMarkup
     */
    public static InlineKeyboardMarkup createKeyboard(
            int pageSize, boolean leftArrowNeed, int startIndex, int lastIndex, boolean rightArrowNeed) {

        return new QuestionViewerKeyboardCreator(pageSize, leftArrowNeed, startIndex, lastIndex, rightArrowNeed)
                .keyboard;
    }

    private void createFirstButton(boolean leftArrowNeed, int startIndex) {
        if (leftArrowNeed) {
            createLeftArrow(startIndex);
        } else {
            createCris(0);
        }
    }

    private void createQuestionsButtons(int startIndex, int lastIndex) {
        int currentButtonIdx = 1;
        for (int i = startIndex; i <= lastIndex; i++) {
            buttons[currentButtonIdx++] = new InlineKeyboardButton(String.valueOf(i + 1))
                    .callbackData("QuestionViewer.Taken index." + (i + 1) + ".Showed first index." + startIndex);
        }
    }

    private void createLastButton(boolean leftArrowNeed, boolean rightArrowNeed, int lastIndex) {
        if (rightArrowNeed) {
            createRightArrow(lastIndex);
        } else {
            if (leftArrowNeed) {
                createCris(buttons.length - 1);
            } else {
                deleteLastButton();
            }
        }
    }

    private void createLeftArrow(int startIndex) {
        buttons[0] = new InlineKeyboardButton("\u25C0")
                .callbackData("QuestionViewer.Switch page to previous.First element index." + startIndex);
    }

    private void createRightArrow(int lastIndex) {
        buttons[buttons.length - 1] =  new InlineKeyboardButton("\u25B6")
                .callbackData("QuestionViewer.Switch page to next.Last element index." + lastIndex);
    }

    private void createCris(int buttonsIdx) {
        buttons[buttonsIdx] = new InlineKeyboardButton("\u274C")
                .callbackData("QuestionViewer.Delete message");
    }

    private void deleteLastButton() {
        buttons = Arrays.copyOf(buttons, buttons.length - 1);
    }
}
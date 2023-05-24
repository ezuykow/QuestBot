package ru.coffeecoders.questbot.properties.viewer;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.Arrays;

/**
 * @author ezuykow
 */
public class PropertiesViewerKeyboard {

    private InlineKeyboardButton[] buttons;
    private final InlineKeyboardMarkup keyboard;

    private PropertiesViewerKeyboard(
            int pageSize, boolean leftArrowNeed, int startIndex, int lastIndex, boolean rightArrowNeed) {

        buttons = new InlineKeyboardButton[pageSize + 2];
        createFirstButton(leftArrowNeed, startIndex);
        createQuestionsButtons(startIndex, lastIndex);
        createLastButton(leftArrowNeed, rightArrowNeed, lastIndex);
        keyboard = new InlineKeyboardMarkup(buttons);
    }

    //-----------------API START-----------------

    /**
     * @author ezuykow
     */
    public static InlineKeyboardMarkup createKeyboard(int pageSize, boolean leftArrowNeed, int startIndex, int lastIndex, boolean rightArrowNeed) {
        return new PropertiesViewerKeyboard(pageSize, leftArrowNeed, startIndex, lastIndex, rightArrowNeed).keyboard;
    }

    //-----------------API END-----------------


    /**
     * @author ezuykow
     */
    private void createFirstButton(boolean leftArrowNeed, int startIndex) {
        if (leftArrowNeed) {
            createLeftArrow(startIndex);
        } else {
            createCris(0);
        }
    }

    /**
     * @author ezuykow
     */
    private void createQuestionsButtons(int startIndex, int lastIndex) {
        int currentButtonIdx = 1;
        for (int i = startIndex; i <= lastIndex; i++) {
            buttons[currentButtonIdx++] = new InlineKeyboardButton(String.valueOf(i + 1))
                    .callbackData("PropertiesViewer.Taken index." + i + ".Showed first index." + startIndex);
        }
    }

    /**
     * @author ezuykow
     */
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

    /**
     * @author ezuykow
     */
    private void createLeftArrow(int startIndex) {
        buttons[0] = new InlineKeyboardButton("\u25C0")
                .callbackData("PropertiesViewer.Switch page to previous.First element index." + startIndex);
    }

    /**
     * @author ezuykow
     */
    private void createRightArrow(int lastIndex) {
        buttons[buttons.length - 1] =  new InlineKeyboardButton("\u25B6")
                .callbackData("PropertiesViewer.Switch page to next.Last element index." + lastIndex);
    }

    /**
     * @author ezuykow
     */
    private void createCris(int buttonsIdx) {
        buttons[buttonsIdx] = new InlineKeyboardButton("\u274C")
                .callbackData("PropertiesViewer.Delete message");
    }

    /**
     * @author ezuykow
     */
    private void deleteLastButton() {
        buttons = Arrays.copyOf(buttons, buttons.length - 1);
    }
}

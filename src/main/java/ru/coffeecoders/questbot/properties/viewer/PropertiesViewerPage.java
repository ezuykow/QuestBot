package ru.coffeecoders.questbot.properties.viewer;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import ru.coffeecoders.questbot.properties.PropertyRow;

import java.util.List;

/**
 * @author ezuykow
 */
public class PropertiesViewerPage {

    private final List<PropertyRow> properties;
    private final int pageSize;
    private final int startIndex;
    private final int pagesCount;
    private final int defaultPageSize;

    private final int lastIndex;
    private boolean leftArrowNeed;
    private boolean rightArrowNeed;

    private String text;
    private InlineKeyboardMarkup keyboard;

    public PropertiesViewerPage(List<PropertyRow> properties, int pageSize, int startIndex, int pagesCount, int defaultPageSize) {
        this.properties = properties;
        this.pageSize = pageSize;
        this.startIndex = startIndex;
        this.pagesCount = pagesCount;
        this.defaultPageSize = defaultPageSize;

        lastIndex = Math.min(startIndex + this.pageSize - 1, properties.size() - 1);
        createText();
        checkArrowsNeed();
        createKeyboard();
    }

    //-----------------API START-----------------

    /**
     * @author ezuykow
     */
    public static PropertiesViewerPage createPage(List<PropertyRow> properties, int pageSize, int startIndex, int pagesCount, int defaultPageSize) {
        return new PropertiesViewerPage(properties, pageSize, startIndex, pagesCount, defaultPageSize);
    }

    public String getText() {
        return text;
    }

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
                    .append(properties.get(i).getDescription())
                    .append("\n");
        }
        text = sb.toString();
    }

    /**
     * @author ezuykow
     */
    private String calcPage() {
        int currentPage = (startIndex / defaultPageSize) + 1;
        return "Страница " + currentPage + " из " + pagesCount + "\n\n";
    }

    /**
     * @author ezuykow
     */
    private void checkArrowsNeed() {
        leftArrowNeed = startIndex != 0;
        rightArrowNeed = lastIndex != properties.size() - 1;
    }

    /**
     * @author ezuykow
     */
    private void createKeyboard() {
        keyboard = PropertiesViewerKeyboard.createKeyboard(
                pageSize, leftArrowNeed, startIndex, lastIndex, rightArrowNeed);
    }
}

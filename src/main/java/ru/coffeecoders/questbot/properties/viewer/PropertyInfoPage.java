package ru.coffeecoders.questbot.properties.viewer;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import ru.coffeecoders.questbot.properties.PropertyRow;

/**
 * @author ezuykow
 */
public class PropertyInfoPage {

    private PropertyRow propertyRow;
    private String text;
    private InlineKeyboardMarkup keyboard;

    private PropertyInfoPage() {}

    //-----------------API START-----------------

    /**
     * @author ezuykow
     */
    public static PropertyInfoPage createPage(PropertyRow propertyRow, int targetPropertyIdx) {
        PropertyInfoPage page = new PropertyInfoPage();
        page.propertyRow = propertyRow;

        page.createText();
        page.createKeyboard(targetPropertyIdx);

        return page;
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
        text = "✔ Описание: " + propertyRow.getDescription() +
                "\n❗ Используемое значение:\n" + propertyRow.getActualProperty() +
                "\n❓ Дефолтное значение:\n" + propertyRow.getDefaultProperty();
    }

    /**
     * @author ezuykow
     */
    private void createKeyboard(int targetPropertyIdx) {
        keyboard = PropertyInfoKeyboard.createKeyboard(targetPropertyIdx);
    }
}

package ru.coffeecoders.questbot.properties.viewer;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

/**
 * @author ezuykow
 */
public class PropertyInfoKeyboard {

    private final InlineKeyboardMarkup keyboard;

    private PropertyInfoKeyboard(int targetPropertyIdx) {
        keyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton("Редактировать")
                        .callbackData("PropertiesViewer.Property info.Edit." + targetPropertyIdx),
                new InlineKeyboardButton("Восстановить")
                        .callbackData("PropertiesViewer.Property info.Restore." + targetPropertyIdx),
                new InlineKeyboardButton(Character.toString(0x1F519))
                        .callbackData("PropertiesViewer.Property Info.Back"));
    }

    //-----------------API START-----------------

    /**
     * @author ezuykow
     */
    public static InlineKeyboardMarkup createKeyboard(int targetPropertyIdx) {
        return new PropertyInfoKeyboard(targetPropertyIdx).keyboard;
    }

    //-----------------API END-----------------
}

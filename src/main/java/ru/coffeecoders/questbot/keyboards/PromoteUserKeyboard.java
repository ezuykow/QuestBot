package ru.coffeecoders.questbot.keyboards;

import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.*;

import java.util.Set;

/**
 * @author ezuykow
 */
public class PromoteUserKeyboard {

    private final InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

    private PromoteUserKeyboard(Set<User> users) {
        createButtons(users);
    }

    /**
     * @author ezuykow
     */
    public static InlineKeyboardMarkup createKeyboard(Set<User> users) {
        return new PromoteUserKeyboard(users)
                .keyboard;
    }

    /**
     * @author ezuykow
     */
    private void createButtons(Set<User> users) {
        for (User user : users) {
            keyboard.addRow(
                    new InlineKeyboardButton(getName(user))
                            .callbackData("PromoteUser." + user.id())
            );
        }
    }

    /**
     * @author ezuykow
     */
    private String getName(User user) {
        String name = user.firstName();
        if (user.lastName() != null) {
            name += " ".concat(user.lastName());
        }
        return name;
    }
}

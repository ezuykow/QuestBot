package ru.coffeecoders.questbot.keyboards;

import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.*;

import java.util.Set;

/**
 * @author ezuykow
 */
public class PromoteOrDemoteUserKeyboard {

    private final InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

    private PromoteOrDemoteUserKeyboard(Set<User> users, String dataPrefix) {
        createButtons(users, dataPrefix);
    }

    /**
     * @author ezuykow
     */
    public static InlineKeyboardMarkup createKeyboard(Set<User> users, String dataPrefix) {
        return new PromoteOrDemoteUserKeyboard(users, dataPrefix)
                .keyboard;
    }

    /**
     * @author ezuykow
     */
    private void createButtons(Set<User> users, String dataPrefix) {
        for (User user : users) {
            String name = getName(user);
            keyboard.addRow(
                    new InlineKeyboardButton(name)
                            .callbackData(dataPrefix + name + "." + user.id())
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

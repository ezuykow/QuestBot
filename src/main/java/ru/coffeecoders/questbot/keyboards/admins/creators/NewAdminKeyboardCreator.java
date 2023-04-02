package ru.coffeecoders.questbot.keyboards.admins.creators;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewAdminKeyboardCreator {

    static List<String[]> peoplesInAdmin;

    private static InlineKeyboardButton[] buttons = new InlineKeyboardButton[0];

    public static InlineKeyboardButton newAdminButtonCreate(String firstName, String lastName, String username) {
        InlineKeyboardButton button = new InlineKeyboardButton(firstName + " " + lastName + " - " + username);
        button.callbackData("newadmin " + username);
        return button;
    }

    public static void addButton(InlineKeyboardButton button) {
        InlineKeyboardButton[] newButtons = new InlineKeyboardButton[buttons.length + 1];
        System.arraycopy(buttons, 0, newButtons, 0, buttons.length);
        newButtons[buttons.length] = button;
        buttons = newButtons;
    }

    public static InlineKeyboardMarkup createKeyboard() {
        InlineKeyboardButton[][] keyboardButtons = new InlineKeyboardButton[buttons.length][1];
        for (int i = 0; i < buttons.length; i++) {
            keyboardButtons[i][0] = buttons[i];
        }
        return new InlineKeyboardMarkup(keyboardButtons);
    }

    public static InlineKeyboardMarkup newAdminKeyboardCreate() {
        for (String[] user : peoplesInAdmin) {
            String firstName = user[0];
            String lastName = user[1];
            String username = user[2];
            addButton(newAdminButtonCreate(firstName, lastName, username));
        }
        return createKeyboard();
    }

}
    //TODO создавать инлайн клаву с текстом "Выберите нового админа" и кнопками - Имя+username
    // метод принимает FirstName LastName, и Имя пользователя
    //CallbackQuery: "newadmin <user_id>"



}

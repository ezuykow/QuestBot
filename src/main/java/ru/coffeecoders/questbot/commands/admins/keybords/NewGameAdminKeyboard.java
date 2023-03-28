package ru.coffeecoders.questbot.commands.admins.keybords;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

public class NewGameAdminKeyboard {


    private final DefaultAdminKeyboard defaultAmKb;

    public NewGameAdminKeyboard(DefaultAdminKeyboard defaultAmKb) {
        this.defaultAmKb = defaultAmKb;
    }

    public void newGameCommand(long chatId, int userId) {
        if (isAdmin(userId)) {
            KeyboardButton returnToMain = new KeyboardButton("Вернуться в главное меню");
            //TODO логика кнопки возвратить в главное меню, перенос текста
            String replyText = "Выберите тип игры:";
            KeyboardButton freeModeButton = new KeyboardButton("Свободный режим");
            //TODO логика кнопки Свободный режим, перенос текста
            KeyboardButton byStationsButton = new KeyboardButton("Игра по станциям");
            //TODO логика кнопки Игра по станциям, перенос текста
            KeyboardButton backButton = new KeyboardButton("Назад");
            //TODO логика кнопки Назад (своя), перенос текста , перенос текста
            KeyboardButton[] buttonsRow1 = new KeyboardButton[]{freeModeButton, byStationsButton};
            KeyboardButton[]  buttonsRow2 = new KeyboardButton[]{backButton,returnToMain};
            Keyboard keyboardMarkup = new ReplyKeyboardMarkup(buttonsRow1, buttonsRow2).selective(true);
            //TODO селектив включён - логику надо понять как настроить список селектив, перенос текст


            //SendMessage request = new SendMessage(chatId, replyText).replyMarkup(keyboardMarkup);
            //msgSender.poll(request);
        } else {
            defaultAmKb.defaultKeyboardNotAdmin(chatId);
        }

    }
}


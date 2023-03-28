package ru.coffeecoders.questbot.commands.admins.keybords;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Component;

@Component
public class DefaultAdminKeyboard {

    private final AdminCommandsMsgSender msgSender;

    public DefaultAdminKeyboard(AdminCommandsMsgSender msgSender) {
        this.msgSender = msgSender;
    }

    private final KeyboardButton visitChatButton = new KeyboardButton("Посетить чат сообщества");
    //логика кнопки Игра по станциям
    private final KeyboardButton returnKeyboardButton = new KeyboardButton("Вернуть клавиатуру");

    public void defaultKeyboardNotAdmin(long chatId) {
        String replyText = "Это технический код, вы можете посетить наше сообщество";
        KeyboardButton[] buttonsRow1 = new KeyboardButton[]{visitChatButton};
        KeyboardButton[] buttonsRow2 = new KeyboardButton[]{returnKeyboardButton};
        Keyboard keyboardMarkup = new ReplyKeyboardMarkup(buttonsRow1, buttonsRow2).selective(true);
        //SendMessage request = new SendMessage(chatId, replyText).replyMarkup(keyboardMarkup);
        //SendResponse response = msgSender.send(request);
    }

    public void exitToKeyboardCommand(long chatId) {
        ReplyKeyboardRemove keyboardRemove = new ReplyKeyboardRemove();
        //SendMessage request = new SendMessage(chatId, "Вернуть клавиатуру").replyMarkup(keyboardRemove);
        //SendResponse response = msgSender.send(request);
    }
}

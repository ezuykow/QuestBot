package ru.coffeecoders.questbot.keyboards.general.creators;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ChatTypeSelectKeyboard {
    @Value("${keyboard.startBoard.globalChat}")
    private static String global;
    @Value("${keyboard.startBoard.adminChat}")
    private static String admins;

    private ChatTypeSelectKeyboard chatTypeSelectKeyboard;

    private ChatTypeSelectKeyboard(ChatTypeSelectKeyboard chatTypeSelectKeyboard) {
        this.chatTypeSelectKeyboard = chatTypeSelectKeyboard;
    }

    public ChatTypeSelectKeyboard getChatTypeSelectKeyboard() {
        return chatTypeSelectKeyboard;
    }

    public static Keyboard createChatTypeSelectKeyboard() {
        KeyboardButton[] buttonArray = makeButtonArray();
        KeyboardButton[][] buttonRows = makeRows(buttonArray);
        ReplyKeyboardMarkup keyboardMarkup = makeKeyboard(buttonRows);
        return keyboardMarkup;
    }

    private static KeyboardButton[] makeButtonArray() {
        KeyboardButton returnKb = new KeyboardButton(admins);
        KeyboardButton toGroup = new KeyboardButton(global);
        return new KeyboardButton[] {toGroup, returnKb};
    }

    private static KeyboardButton[][] makeRows(KeyboardButton[] buttonArray) {
        KeyboardButton[] firstRow = new KeyboardButton[] {buttonArray[0]};
        KeyboardButton[] secondRow = new KeyboardButton[] {buttonArray[1]};
        return new KeyboardButton[][] {firstRow, secondRow};
    }

    private static ReplyKeyboardMarkup makeKeyboard(KeyboardButton[][] buttonRows) {
        return new ReplyKeyboardMarkup(buttonRows);
    }
}

package ru.coffeecoders.questbot.commands.admins.keybords;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Value;
import ru.coffeecoders.questbot.commands.admins.keybords.creators.Keyboard;

public class NewGameKeyboard implements Keyboard {

    private final AdminCommandsMsgSender msgSender;
    private NewGameKeyboard newGameKeyboard;
    public NewGameKeyboard getNewGameKeyboard() {
        return newGameKeyboard;
    }

    private NewGameKeyboard() {
    }
    @Value("${keyboard.newGame.annotation}")
    private String buttonName2;
    @Value("${keyboard.newGame.return}")
    private String buttonReturn;
    @Value("${keyboard.newGame.freeMode}")
    private String buttonFreeMode;


    public void createNewGameCommand(long chatId, int userId) {
        if (isAdmin(userId)) {
            String replyText = buttonName2;
            KeyboardButton returnToMain = new KeyboardButton(buttonReturn);
            KeyboardButton freeModeButton = new KeyboardButton(buttonName3);
            KeyboardButton byStationsButton = new KeyboardButton("Игра по станциям");
            KeyboardButton backButton = new KeyboardButton("Назад");

            KeyboardButton[] buttonsRow1 = new KeyboardButton[]{freeModeButton, byStationsButton};
            KeyboardButton[]  buttonsRow2 = new KeyboardButton[]{backButton,returnToMain};

            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(buttonsRow1, buttonsRow2).selective(true);

            SendMessage request = new SendMessage(chatId, replyText).replyMarkup(keyboardMarkup);
            msgSender.send(request);
        } else {
            String replyText = "Это технический код, вы можете посетить наше сообщество";
            KeyboardButton visitChatButton = new KeyboardButton("Посетить телеграм канал");
            KeyboardButton returnKeyboardButton = new KeyboardButton("Вернуть клавиатуру");

            KeyboardButton[] buttonsRow1 = new KeyboardButton[]{visitChatButton};
            KeyboardButton[] buttonsRow2 = new KeyboardButton[]{returnKeyboardButton};

            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(buttonsRow1, buttonsRow2).selective(true);
            SendMessage request = new SendMessage(chatId, replyText).replyMarkup(keyboardMarkup);
            msgSender.send(request);
        }

    }
}



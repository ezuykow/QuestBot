package ru.coffeecoders.questbot.commands.admins.keybords;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.commands.admins.keybords.DefaultAdminKeyboard;
import ru.coffeecoders.questbot.commands.admins.keybords.NewGameAdminKeyboard;

@Component
public class AdminsCommandActions {

    /**
     * Методы
     * newGameCommand(long chatId, int userId)
     * alreadyRunningGamesCommand(long chatId, int userId)
     * questionsCommand(long chatId, int userId)
     * defaultKeyboardNotAdmin(long chatId)
     */
    private final KeyboardButton visitChatButton = new KeyboardButton("Посетить чат сообщества");
    //логика кнопки Игра по станциям
    private final KeyboardButton returnKeyboardButton = new KeyboardButton("Вернуть клавиатуру");
    //логика кнопки Игра по станциям
    private final KeyboardButton returnToMain = new KeyboardButton("Вернуться в главное меню");
    //логика кнопки возвратить в главное меню
    private String replyText;
    private final AdminCommandsMsgSender msgSender;
    private final DefaultAdminKeyboard defaultAmKb;
    private final NewGameAdminKeyboard newGameAmKb;
    public AdminsCommandActions(AdminCommandsMsgSender msgSender, DefaultAdminKeyboard defaultAdminKeyboard, NewGameAdminKeyboard newGameAmKb) {

        this.msgSender = msgSender;
        this.defaultAmKb = defaultAdminKeyboard;
        this.newGameAmKb = newGameAmKb;
    }


    //TODO msgSender.getAllGames();
    //TODO msgSender.send(request);
    //TODO ??.isAdmin(userId)


    private void defaultNotAdmin(long chatId) {
        defaultAmKb.defaultKeyboardNotAdmin(chatId);
    }
    public void newGameCommand(long chatId, int userId) {

    }

    public void questionsCommand(long chatId, int userId) {
        if (isAdmin(userId)) {

        } else {
            defaultAmKb.defaultKeyboardNotAdmin(chatId);
        }
    }






    public void allRunningGamesCommand(long chatId, int userId) {
        if (isAdmin(userId)) {

        } else {
            defaultKeyboardNotAdmin(chatId);
        }
    }

// нужно решить по сколько вопросов выводит бот //
    public void showQuestionList(long chatId, int userId) {
        if (isAdmin(userId)) {

        } else {
            defaultKeyboardNotAdmin(chatId);
        }

    }



}

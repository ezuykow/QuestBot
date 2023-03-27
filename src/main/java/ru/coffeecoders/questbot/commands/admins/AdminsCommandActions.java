package ru.coffeecoders.questbot.commands.admins;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;



public class AdminsCommandActions {

    /**
     * Методы
     * newGameCommand()
     * alreadyRunningGamesCommand()
     * questionsCommand()
     */
    private final KeyboardButton visitChatButton = new KeyboardButton("Посетить чат сообщества");
    //логика кнопки Игра по станциям
    private final KeyboardButton returnKeyboardButton = new KeyboardButton("Вернуть клавиатуру");
    //логика кнопки Игра по станциям
    private String replyText;
    private final AdminCommandsMsgSender msgSender;
    //msgSender.getAllGames();
    //msgSender.send(request);
    //??.isAdmin(userId)

    public AdminsCommandActions(AdminCommandsMsgSender msgSender) {
        this.msgSender = msgSender;
    }

    public void newGameCommand(long chatId, int userId) {

       if(isAdmin(userId)) {
            replyText = "Выберите тип игры:";
            KeyboardButton freeModeButton = new KeyboardButton("Свободный режим");
            //логика кнопки Свободный режим
            KeyboardButton byStationsButton = new KeyboardButton("Игра по станциям");
            //логика кнопки Игра по станциям
            KeyboardButton backButton = new KeyboardButton("Назад");
            //логика кнопки Назад
            KeyboardButton[] buttonsRow1 = new KeyboardButton[]{freeModeButton, byStationsButton};
            KeyboardButton[]  buttonsRow2 = new KeyboardButton[]{backButton};
            Keyboard keyboardMarkup = new ReplyKeyboardMarkup(buttonsRow1, buttonsRow2).selective(true);
            //селектив включён - логику надо понять как настроить список селектив, в процессе.

            SendMessage request = new SendMessage(chatId, replyText).replyMarkup(keyboardMarkup);
            SendResponse response = msgSender.send(request);
        } else {
           replyText = "Это технический чат, вы можете посетить наше сообщество";
           //логика кнопки Игра по станциям
           defaultKeyboardNotAdmin(chatId);
       }
    }


    public void allRunningGamesCommand(long chatId, int userId) {

        if (isAdmin(userId)) {
            String replyText = msgSender.getAllGames();
            SendMessage request = new SendMessage(chatId, replyText);
            SendResponse response = msgSender.send(request);
        } else {
            defaultKeyboardNotAdmin(chatId);
        }
    }

    private void defaultKeyboardNotAdmin(long chatId) {
        replyText = "Это технический чат, вы можете посетить наше сообщество";
        KeyboardButton[] buttonsRow1 = new KeyboardButton[]{visitChatButton};
        KeyboardButton[] buttonsRow2 = new KeyboardButton[]{returnKeyboardButton};
        Keyboard keyboardMarkup = new ReplyKeyboardMarkup(buttonsRow1, buttonsRow2).selective(true);
        SendMessage request = new SendMessage(chatId, replyText).replyMarkup(keyboardMarkup);
        SendResponse response = msgSender.send(request);
    }

    public void questionsCommand(long chatId) {
        replyText = "Выберите действие:";
        KeyboardButton openBankButton = new KeyboardButton("Открыть банк вопросов");
        //логика кнопки "Открыть банк вопросов"
        KeyboardButton addQuestionButton = new KeyboardButton("Добавить вопрос");
        //логика кнопки "Добавить вопрос"
        KeyboardButton[] buttonsRow1 = new KeyboardButton[]{openBankButton,addQuestionButton};
        KeyboardButton[] buttonsRow2 = new KeyboardButton[]{returnKeyboardButton};
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(buttonsRow1, buttonsRow2);
        SendMessage request = new SendMessage(chatId, replyText).replyMarkup(keyboardMarkup);
        SendResponse response = msgSender.send(request);
    }

    public void exitToKeyboardCommand(long chatId) {
        ReplyKeyboardRemove keyboardRemove = new ReplyKeyboardRemove();
        SendMessage request = new SendMessage(chatId, "Вернуть клавиатуру").replyMarkup(keyboardRemove);
        SendResponse response = msgSender.send(request);
    }
}

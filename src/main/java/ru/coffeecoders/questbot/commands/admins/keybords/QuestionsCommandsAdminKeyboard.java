package ru.coffeecoders.questbot.commands.admins.keybords;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

public class QuestionsCommandsAdminKeyboard {

    public QuestionsCommandsAdminKeyboard(DefaultAdminKeyboard defaultAmKb) {
        this.defaultAmKb = defaultAmKb;
    }

    private final DefaultAdminKeyboard defaultAmKb;

    public void questionsCommand(long chatId, int userId) {
        if (isAdmin(userId)) {
            String replyText = "Выберите действие:";
            KeyboardButton openBankButton = new KeyboardButton("Открыть банк вопросов");
            //логика кнопки "Открыть банк вопросов" - тут переход в метод
            KeyboardButton addQuestionButton = new KeyboardButton("Добавить вопрос");
            //логика кнопки "Добавить вопрос"
            KeyboardButton backButton = new KeyboardButton("Назад");
            //логика кнопки Назад (своя)
            KeyboardButton[] buttonsRow1 = new KeyboardButton[]{openBankButton, addQuestionButton};
            KeyboardButton[] buttonsRow2 = new KeyboardButton[]{backButton, returnToMain};
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(buttonsRow1, buttonsRow2);
            SendMessage request = new SendMessage(chatId, replyText).replyMarkup(keyboardMarkup);
            SendResponse response = msgSender.poll(request);

        } else {
            defaultAmKb.defaultKeyboardNotAdmin(chatId);
        }
    }

    public void allRunningGamesCommand(long chatId, int userId) {

        if (isAdmin(userId)) {
            String replyText = msgSender.getAllGames();
            SendMessage request = new SendMessage(chatId, replyText);
            SendResponse response = msgSender.poll(request);
        } else {
            defaultAmKb.defaultKeyboardNotAdmin(chatId);
        }
    }


}
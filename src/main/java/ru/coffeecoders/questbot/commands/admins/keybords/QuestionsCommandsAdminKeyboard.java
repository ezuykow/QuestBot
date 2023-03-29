package ru.coffeecoders.questbot.commands.admins.keybords;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import ru.coffeecoders.questbot.commands.admins.keybords.DefaultAdminKeyboard;

public class QuestionsCommandsAdminKeyboard {

    public QuestionsCommandsAdminKeyboard(DefaultAdminKeyboard defaultAmKb) {
        this.defaultAmKb = defaultAmKb;
    }

    private final DefaultAdminKeyboard defaultAmKb;
    //TODO логика кнопки "Открыть банк вопросов" - тут переход в метод"Добавить вопрос"кнопки Назад (своя)
    public void questionsCommand(long chatId, int userId) {
        if (isAdmin(userId)) {
            String replyText = "Выберите действие:";
            KeyboardButton openBankButton = new KeyboardButton("Открыть банк вопросов");
            KeyboardButton addQuestionButton = new KeyboardButton("Добавить вопрос");
            KeyboardButton backButton = new KeyboardButton("Назад");
            KeyboardButton[] buttonsRow1 = new KeyboardButton[]{openBankButton, addQuestionButton};
            KeyboardButton[] buttonsRow2 = new KeyboardButton[]{backButton, returnToMain};
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(buttonsRow1, buttonsRow2);
            SendMessage request = new SendMessage(chatId, replyText).replyMarkup(keyboardMarkup);
            msgSender.poll(request);

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
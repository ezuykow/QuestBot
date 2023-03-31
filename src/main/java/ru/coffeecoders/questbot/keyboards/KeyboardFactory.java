package ru.coffeecoders.questbot.keyboards;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.keyboards.admins.creators.MainAdminsKeyboardCreator;
import ru.coffeecoders.questbot.keyboards.admins.creators.NewGameKeyboardCreator;
import ru.coffeecoders.questbot.keyboards.admins.creators.QuestionsMenuKeyboardCreator;
import ru.coffeecoders.questbot.keyboards.general.creators.ChatTypeSelectKeyboard;

@Component
public class KeyboardFactory {

    public enum KeyboardType {
        START,
        NEW_ADMIN,
        NEW_GAME,
        MAIN_ADMIN,
        QUESTIONS_MENU
    }

    private KeyboardFactory(KeyboardSender keyboardSender) {
        this.keyboardSender = keyboardSender;
    }

    private KeyboardFactory() {
    }

    private KeyboardSender keyboardSender;

    public KeyboardSender getKeyboardSender() {
        return keyboardSender;
    }

    //TODO String replyText = "Это технический чат, вы можете посетить наше сообщество";
    //TODO String reply String replyText = "Выберите действие:";
    public void createKeyboard(KeyboardType keyboardType, Update update) {
        switch (keyboardType) {
            case START -> keyboardSender.sendKeyboard(ChatTypeSelectKeyboard.createChatTypeSelectKeyboard(), update);
            case NEW_GAME -> keyboardSender.sendKeyboard(NewGameKeyboardCreator.newGameKeyboardCreate(), update);
            case QUESTIONS_MENU -> keyboardSender.sendKeyboard(QuestionsMenuKeyboardCreator.createQuestionKeyboard(), update);
            case MAIN_ADMIN -> keyboardSender.sendKeyboard(MainAdminsKeyboardCreator.MainKeyboardCreate(), update);
        }
    }
}


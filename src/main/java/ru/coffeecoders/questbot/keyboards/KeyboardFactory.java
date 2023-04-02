package ru.coffeecoders.questbot.keyboards;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.keyboards.admins.creators.*;
import ru.coffeecoders.questbot.keyboards.general.creators.ChatTypeSelectKeyboard;

@Component
public class KeyboardFactory {

    public enum KeyboardType {
        START,
        NEW_ADMIN,
        NEW_GAME,
        MAIN_ADMIN,
        QUESTIONS_MENU,
        ADDQUESTION,
        SHOWQUESTIONS,
        EDITKEYBOARD

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

    public void createKeyboard(KeyboardType keyboardType, Update update) {
        switch (keyboardType) {
            case START -> keyboardSender.sendKeyboard(ChatTypeSelectKeyboard.createChatTypeSelectKeyboard(), update);
            case NEW_GAME -> keyboardSender.sendKeyboard(NewGameKeyboardCreator.newGameKeyboardCreate(), update);
            case QUESTIONS_MENU -> keyboardSender.sendKeyboard(AddKeyboardCreator.createQuestionKeyboard(), update);
            case MAIN_ADMIN -> keyboardSender.sendKeyboard(MainAdminsKeyboardCreator.mainKeyboardCreate(), update);
            case NEW_ADMIN -> keyboardSender.sendKeyboard(NewAdminKeyboardCreator.newAdminKeyboardCreate(), update);


            case SHOWQUESTIONS -> keyboardSender.sendKeyboard(ViewQuestionsUpdateCreator.viewKeyboardCreate(),  update);
            case EDITKEYBOARD -> keyboardSender.sendKeyboard(EditDeleteKeyboardCreator.createQuestionKeyboard(),  update);

        }
    }
}


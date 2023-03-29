package ru.coffeecoders.questbot.commands.admins.keybords.keyboards;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.commands.admins.keybords.keyboards.creators.*;

@Component
public class KeyboardFactory implements Keyboard {

    public enum KeyboardType {
        NEW_GAME,MAIN_ADMIN,QUESTIONS_MENU
    }

    private KeyboardFactory(KeyboardSender keyboardSender) {
        this.keyboardSender = keyboardSender;
    }

    public KeyboardFactory() {
    }

    private KeyboardSender keyboardSender;

    public KeyboardSender getKeyboardSender() {
        return keyboardSender;
    }

    public void createKeyboard(KeyboardType keyboardType){
        switch (keyboardType){
            // String replyText = "Это технический чат, вы можете посетить наше сообщество";
            //reply         String replyText = "Выберите действие:";
            case NEW_GAME -> keyboardSender.sendKeyboard((Keyboard) NewGameKeyboardCreator.newGameKeyboardCreate());
            case QUESTIONS_MENU -> keyboardSender.sendKeyboard((Keyboard) QuestionKeyboardCreator.createQuestionKeyboard());
            case MAIN_ADMIN -> keyboardSender.sendKeyboard((Keyboard) MainKeyboardCreator.MainKeyboardCreate());
            default ->  keyboardSender.sendKeyboard((Keyboard) DefaultKeyboardCreator.createDefaultKeyboard());
        }

    }
}



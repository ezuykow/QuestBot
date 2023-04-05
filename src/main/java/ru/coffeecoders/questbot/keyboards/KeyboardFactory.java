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
            case START -> keyboardSender.sendKeyboard(ChatTypeSelectKeyboard.createChatTypeSelectKeyboard(), update.message().chat().id());
            case NEW_GAME -> keyboardSender.sendKeyboard(NewGameKeyboardCreator.newGameKeyboardCreate(), update.message().chat().id());
            case QUESTIONS_MENU -> keyboardSender.sendKeyboard(AddKeyboardCreator.createQuestionKeyboard(), update.message().chat().id());
            case MAIN_ADMIN -> keyboardSender.sendKeyboard(MainAdminsKeyboardCreator.mainKeyboardCreate(), update.message().chat().id());
            case NEW_ADMIN -> keyboardSender.sendKeyboard(NewAdminKeyboardCreator.newAdminKeyboardCreate(), update.message().chat().id());


            case SHOWQUESTIONS -> {
                keyboardSender.sendKeyboard(ViewQuestionsKeyboardCreator.createQuestionsKeyboard(update.message().messageId()), QuestionPaginator.updateQuestions(update.message().messageId()), update.message().chat().id());
            //TODO метод обновления параметров в мапе
            }

            case EDITKEYBOARD -> keyboardSender.sendKeyboard(EditDeleteKeyboardCreator.createQuestionKeyboard(),  update.message().chat().id());

        }
    }
}


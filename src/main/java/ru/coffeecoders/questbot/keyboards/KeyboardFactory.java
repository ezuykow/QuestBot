package ru.coffeecoders.questbot.keyboards;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.keyboards.admins.creators.MainAdminsKeyboardCreator;
import ru.coffeecoders.questbot.keyboards.admins.creators.NewGameKeyboardCreator;
import ru.coffeecoders.questbot.keyboards.admins.creators.KeyboardRESTCreator;
import ru.coffeecoders.questbot.keyboards.admins.creators.ViewQuestionsUpdateAssembly;
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
        SHOWLASTQUESTIONS,

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
            case QUESTIONS_MENU -> keyboardSender.sendKeyboard(KeyboardRESTCreator.createQuestionKeyboard(), update);
            case MAIN_ADMIN -> keyboardSender.sendKeyboard(MainAdminsKeyboardCreator.MainKeyboardCreate(), update);
            case ADDQUESTION -> keyboardSender.sendKeyboard(KeyboardRESTCreator.addQuestions(), update);

            case SHOWQUESTIONS -> keyboardSender.sendTextAndKeyboard(ViewQuestionsUpdateAssembly.pager(),  update, ViewQuestionsUpdateAssembly.getQuestionsFromIndex()); //  им же переходим на следующую страницу
            case SHOWLASTQUESTIONS -> keyboardSender.sendTextAndKeyboard(ViewQuestionsUpdateAssembly.lastPager(),  update, ViewQuestionsUpdateAssembly.getQuestionsFromIndex());

        }
    }
}


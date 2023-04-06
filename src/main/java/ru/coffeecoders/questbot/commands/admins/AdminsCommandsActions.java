package ru.coffeecoders.questbot.commands.admins;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.keyboards.KeyboardFactory;

@Component
public class AdminsCommandsActions {

    private final KeyboardFactory keyboardFactory;

    public AdminsCommandsActions(KeyboardFactory keyboardFactory) {
        this.keyboardFactory = keyboardFactory;
    }

    public void performNewAdminCmd(Update update) {
        keyboardFactory.createKeyboard(KeyboardFactory.KeyboardType.NEW_ADMIN,update);
    }

    public void performNewGameCmd(Update update) {
        keyboardFactory.createKeyboard(KeyboardFactory.KeyboardType.NEW_GAME, update);
    }

    public void performStartCmd(Update update) {
        keyboardFactory.createKeyboard(KeyboardFactory.KeyboardType.START, update);
    }

   public void performMainCmd(Update update) {
        keyboardFactory.createKeyboard(KeyboardFactory.KeyboardType.MAIN_ADMIN, update);
    }

    public void performNewAddQuestionCmd(Update update) {
        keyboardFactory.createKeyboard(KeyboardFactory.KeyboardType.ADDQUESTION, update);
    }

    public void performShowQuestionCmd(Update update) {
        keyboardFactory.createKeyboard(KeyboardFactory.KeyboardType.SHOWQUESTIONS, update);
    }

    public void performQuestionMenuCmd(Update update) {
        keyboardFactory.createKeyboard(KeyboardFactory.KeyboardType.QUESTIONS_MENU, update);
    }

    public void performEditQuestionCmd(Update update) {
        keyboardFactory.createKeyboard(KeyboardFactory.KeyboardType.EDITKEYBOARD, update);
    }

    public void performStartGameCmd (Long chatId){
        //TODO метод для запуска распределения на команды
    };
    public void  performStarTeamMakerCmd (Long chatId){
        //TODO метод для запуска распределения на команды
    };

    public void performStopGameCmd (Long chatId){
        //TODO метод для запуска игры распределения на команды
    };

}

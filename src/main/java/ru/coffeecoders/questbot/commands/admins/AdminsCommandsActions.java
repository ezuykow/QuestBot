package ru.coffeecoders.questbot.commands.admins;

import com.pengrad.telegrambot.model.Update;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.keyboards.KeyboardSender;
import ru.coffeecoders.questbot.keyboards.admins.creators.EditDeleteKeyboardCreator;
import ru.coffeecoders.questbot.keyboards.admins.creators.NewAdminKeyboardCreator;
import ru.coffeecoders.questbot.keyboards.admins.creators.NewTeamKeyboard;
import ru.coffeecoders.questbot.msg.senders.MessageSender;

import java.util.List;

@Component
public class AdminsCommandsActions {

    private final KeyboardSender keyboardSender;
    private final MessageSender msgSender;
    private final Environment env;

    private AdminsCommandsActions(KeyboardSender keyboardSender, MessageSender msgSender, Environment env) {
        this.keyboardSender = keyboardSender;
        this.msgSender = msgSender;
        this.env = env;
    }

    // добавление нового админа
    public void performNewAdminCmd(Update update, List<String[]> users) {
        keyboardSender.sendKeyboard(NewAdminKeyboardCreator.newAdminKeyboardCreate(users), update.message().chat().id());
    }

    public void  performStarTeamMakerCmd (Long chatId, List<String[]> teams){
        keyboardSender.sendKeyboard(NewTeamKeyboard.createKeyboardFromTeams(teams), chatId);
    };

    // удалить/редактировать
    public void performEditQuestionCmd(Update update) {
        keyboardSender.sendKeyboard(EditDeleteKeyboardCreator.createQuestionKeyboard(),  update.message().chat().id());
    }


    //Выбор вида чата.
    public void performStartCmd(Update update) {
        msgSender.send(update.message().chat().id(), env.getProperty("messages.welcome"));
    }

    public void performNewTeamKeyboard() {

    }
    public void performNewGameCmd(Update update) {
        // TODO keyboardFactory.createKeyboard(KeyboardFactory.KeyboardType.NEW_GAME, update);
    }
   public void performMainCmd(Update update) {
       // TODO keyboardFactory.createKeyboard(KeyboardFactory.KeyboardType.MAIN_ADMIN, update);
    }

    public void performNewAddQuestionCmd(Update update) {
        // TODO   keyboardFactory.createKeyboard(KeyboardFactory.KeyboardType.ADDQUESTION, update);
    }

    public void performShowQuestionCmd(Update update) {
        // TODO   keyboardFactory.createKeyboard(KeyboardFactory.KeyboardType.SHOWQUESTIONS, update);
    }

    public void performQuestionMenuCmd(Update update) {
        // TODO  keyboardFactory.createKeyboard(KeyboardFactory.KeyboardType.QUESTIONS_MENU, update);
    }



    public void performStartGameCmd (Long chatId){
        //TODO метод для запуска распределения на команды
    };

    public void performStopGameCmd (Long chatId){
        //TODO метод для запуска игры распределения на команды
    };

}

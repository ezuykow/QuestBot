package ru.coffeecoders.questbot.commands.admins;

import com.pengrad.telegrambot.model.Update;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.msg.senders.MessageSender;

import java.util.List;

@Component
public class AdminsCommandsActions {

    private final MessageSender msgSender;
    private final Environment env;

    private AdminsCommandsActions(MessageSender msgSender, Environment env) {
        this.msgSender = msgSender;
        this.env = env;
    }

    public void performNewAdminCmd(Update update) {
        //TODO msgSender.send(update.message().chat().id(), "Выберите ...", NewAdminKb.createKb);
    }

    public void  performStarTeamMakerCmd (Long chatId, List<String[]> teams){
    };

    public void performEditQuestionCmd(Update update) {
    }

    public void performStartCmd(Update update) {
        msgSender.send(update.message().chat().id(), env.getProperty("messages.welcome"));
    }

    public void performNewTeamKeyboard() {

    }
    public void performNewGameCmd(Update update) {
    }
   public void performMainCmd(Update update) {
    }

    public void performNewAddQuestionCmd(Update update) {
    }

    public void performShowQuestionCmd(Update update) {
    }

    public void performQuestionMenuCmd(Update update) {
    }

    public void performStartGameCmd (Long chatId){
        //TODO метод для запуска распределения на команды
    };

    public void performStopGameCmd (Long chatId){
        //TODO метод для запуска игры распределения на команды
    };

}

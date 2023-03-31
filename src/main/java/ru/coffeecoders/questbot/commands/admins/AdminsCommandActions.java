package ru.coffeecoders.questbot.commands.admins;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.keyboards.KeyboardFactory;

@Component
public class AdminsCommandActions {

// 2.TODO логика открытия и закрытия клавиатур
    //1. TODO new ReplyKeyboardRemove()

    private final KeyboardFactory keyboardFactory;

    public AdminsCommandActions(KeyboardFactory keyboardFactory) {
        this.keyboardFactory = keyboardFactory;
    }

    public void performNewAdminCmd(Update update) {
//    TODO Factory -> NewAdminKB
    }

    public void performNewGameCmd(Update update) {
        keyboardFactory.createKeyboard(KeyboardFactory.KeyboardType.NEW_GAME, update);
    }

    public void performStartCmd(Update update) {
        keyboardFactory.createKeyboard(KeyboardFactory.KeyboardType.MAIN_ADMIN, update);
    }

}

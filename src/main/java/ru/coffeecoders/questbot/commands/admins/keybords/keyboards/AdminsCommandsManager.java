package ru.coffeecoders.questbot.commands.admins.keybords.keyboards;

import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.commands.Commands;
import ru.coffeecoders.questbot.commands.admins.keybords.keyboards.creators.DefaultKeyboardCreator;

@Component
public class AdminsCommandsManager {

    private final KeyboardFactory.KeyboardType keyboardType;
    private final KeyboardFactory keyboardFactory;
    private final DefaultKeyboardCreator defaultKeyboardCreator;



    private AdminsCommandsManager(KeyboardFactory.KeyboardType keyboardType, KeyboardFactory keyboardFactory, DefaultKeyboardCreator defaultKeyboardCreator) {
        this.keyboardType = keyboardType;
        this.keyboardFactory = keyboardFactory;
        this.defaultKeyboardCreator = defaultKeyboardCreator;
    }

    public void chooseKeyboard(Commands.AdminsCommands KbType, Update update) {
            KeyboardFactory.KeyboardType chosenType = null;
            for (KeyboardFactory.KeyboardType type : KeyboardFactory.KeyboardType.values()) {
                if (type.equals(KbType)) {
                    chosenType = type;
                    break;
                }
            }
            if (chosenType != null) {
                keyboardFactory.createKeyboard(chosenType, update);
            }

    }
}



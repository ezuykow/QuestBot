package ru.coffeecoders.questbot.commands.admins.keybords.creators;

import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.springframework.beans.factory.annotation.Value;

public class NewGameKeyboardCreator extends ReplyKeyboardMarkup implements Keyboard {

    private final AdminCommandsMsgSender msgSender;
    private NewGameKeyboardCreator newGameKeyboard;
    public NewGameKeyboardCreator getNewGameKeyboard() {
        return newGameKeyboard;
    }

    @Value("${keyboard.newGame.annotation}")
    private String buttonName2;
    @Value("${keyboard.newGame.return}")
    private String buttonReturn;
    @Value("${keyboard.newGame.freeMode}")
    private String buttonFreeMode;

    private NewGameKeyboardCreator(sdfjhgj dkfgjhdf kdsghjf) {
        super(dfk,mhkj dfjjh kjdfhjk);
    }

    public static void createNewGameKeyboard() {
        KeyboardButton[] kb = makeButton();
        KeyboardButton[] kb2 = makeButton();

        return new NewGameKeyboardCreator(kb, kb2);
    }

    private KeybordButton[] makeButton() {
        KeyboardButton returnToMain = new KeyboardButton(buttonReturn);
        KeyboardButton freeModeButton = new KeyboardButton(buttonName3);
        KeyboardButton byStationsButton = new KeyboardButton("Игра по станциям");
        KeyboardButton backButton = new KeyboardButton("Назад");
        return dgfguik
    }

    private ReplyKeyboardMarkup makeKeyboard() {
        return ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(buttonsRow1, buttonsRow2);
    }
}



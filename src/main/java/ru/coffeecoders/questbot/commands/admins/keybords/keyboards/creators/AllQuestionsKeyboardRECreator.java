package ru.coffeecoders.questbot.commands.admins.keybords.keyboards.creators;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.aspectj.lang.annotation.control.CodeGenerationHint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AllQuestionsKeyboardRECreator implements Keyboard {


    private final AllQuestionsKeyboardRECreator allQuestionsKeyboardRECreator;

    private AllQuestionsKeyboardRECreator(AllQuestionsKeyboardRECreator allQuestionsKeyboardRECreator) {
        this.allQuestionsKeyboardRECreator = allQuestionsKeyboardRECreator;
    }

    public AllQuestionsKeyboardRECreator getAllQuestionsKeyboardRECreator() {
        return allQuestionsKeyboardRECreator;
    }

    private static InlineKeyboardMarkup makeButtonArrow() {
        InlineKeyboardButton one = new InlineKeyboardButton("1");
        InlineKeyboardButton two = new InlineKeyboardButton("2");
        InlineKeyboardButton three = new InlineKeyboardButton("3");
        InlineKeyboardButton four = new InlineKeyboardButton("4");
        InlineKeyboardButton five = new InlineKeyboardButton("5");
        InlineKeyboardButton six = new InlineKeyboardButton("6");

        return new InlineKeyboardMarkup(one, two, three,four, five,six);
    }

    }
}

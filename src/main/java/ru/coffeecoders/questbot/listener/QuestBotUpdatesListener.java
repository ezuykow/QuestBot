package ru.coffeecoders.questbot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SetMyCommands;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.managers.UpdateManager;
import ru.coffeecoders.questbot.senders.MessageSender;

import java.util.List;

@Service
public class QuestBotUpdatesListener implements UpdatesListener {

    private final UpdateManager updateManager;
    private final TelegramBot bot;
    private final MessageSender msgSender;

    public QuestBotUpdatesListener(UpdateManager updateManager, TelegramBot bot, MessageSender msgSender) {
        this.updateManager = updateManager;
        this.msgSender = msgSender;
        bot.execute(new SetMyCommands(
                new BotCommand("regteam", "(Игрок) Создать команду"),
                new BotCommand("jointeam", "(Игрок) Вступить в команду"),
                new BotCommand("start", "(Админ) Запустить бота"),
                new BotCommand("adminon", "(Админ) Сделать текущий чат администраторским"),
                new BotCommand("showquestions", "(Админ) Показать все вопросы")
        ));
        this.bot = bot;
    }

    @PostConstruct
    public void init() {
        bot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(updateManager::performUpdate);
        msgSender.sendDeleteAllMessageToDelete();
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}

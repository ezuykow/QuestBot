package ru.coffeecoders.questbot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SetMyCommands;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.managers.UpdateManager;

import java.util.List;

@Service
public class QuestBotUpdatesListener implements UpdatesListener {

    private final UpdateManager updateManager;
    private final TelegramBot bot;

    public QuestBotUpdatesListener(UpdateManager updateManager, TelegramBot bot) {
        this.updateManager = updateManager;
        bot.execute(new SetMyCommands(
                new BotCommand("jointeam", "(Игрок) Вступить в команду"),
                new BotCommand("start", "(Админ) Запустить бота"),
                new BotCommand("adminon", "(Админ) Сделать текущий чат администраторским")
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
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}

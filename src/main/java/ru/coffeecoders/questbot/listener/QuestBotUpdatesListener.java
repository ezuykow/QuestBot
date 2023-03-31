package ru.coffeecoders.questbot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.managers.UpdateManager;

import java.util.List;

@Service
public class QuestBotUpdatesListener {
    private final Logger logger = LoggerFactory.getLogger(QuestBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final UpdateManager updateManager;

    public QuestBotUpdatesListener(TelegramBot telegramBot, UpdateManager updateManager) {
        this.telegramBot = telegramBot;
        this.updateManager = updateManager;
    }

    public int process(List<Update> updates) {
        updates.forEach(updateManager::performUpdate);

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}

package ru.coffeecoders.questbot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SetMyCommands;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.managers.UpdateManager;
import ru.coffeecoders.questbot.senders.MessageSender;

import java.util.List;

@Service
public class QuestBotUpdatesListener implements UpdatesListener {

    Logger logger = LoggerFactory.getLogger(QuestBotUpdatesListener.class);

    private final UpdateManager updateManager;
    private final TelegramBot bot;
    private final MessageSender msgSender;

    private boolean startUp;

    public QuestBotUpdatesListener(UpdateManager updateManager, TelegramBot bot, MessageSender msgSender) {
        logger.warn("Starting bot...");
        this.updateManager = updateManager;
        this.msgSender = msgSender;
        bot.execute(new SetMyCommands(
                new BotCommand("regteam", "(Игрок) Создать команду"),
                new BotCommand("jointeam", "(Игрок) Вступить в команду"),
                new BotCommand("showgames", "(Админ) Показать все игры"),
                new BotCommand("showquestions", "(Админ) Показать все вопросы"),
                new BotCommand("newgame", "(Админ) Создать новую игру"),
                new BotCommand("stopbot", "(Админ) Остановить бота"),
                new BotCommand("adminon", "(Владелец) Сделать текущий чат администраторским"),
                new BotCommand("adminoff", "(Владелец) Сделать текущий чат не администраторским"),
                new BotCommand("promote", "(Владелец) Назначить администратором бота"),
                new BotCommand("demote", "(Владелец) Назначить администратором бота")
        ));
        this.bot = bot;
        startUp = true;
    }

    //-----------------API START-----------------

    /**
     * @author anatoliy
     * @Redact: ezuykow
     */
    @PostConstruct
    public void init() {
        GetUpdates gu = new GetUpdates().allowedUpdates(
                "message",
                "edited_message",
                "channel_post",
                "edited_channel_post",
                "inline_query",
                "chosen_inline_result",
                "callback_query",
                "shipping_query",
                "pre_checkout_query",
                "poll",
                "poll_answer",
                "my_chat_member",
                "chat_member",
                "chat_join_request"
        );
        bot.setUpdatesListener(this, gu);
        logger.warn("Bot has been started!");
        msgSender.sendStartUp();
    }

    /**
     * @author anatoliy
     * @Redact: ezuykow
     */
    @Override
    public int process(List<Update> updates) {
        if (startUp) {
            startUp = false;
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }
        updates.forEach(updateManager::performUpdate);
        msgSender.sendDeleteAllMessageToDelete();
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    //-----------------API END-----------------

}

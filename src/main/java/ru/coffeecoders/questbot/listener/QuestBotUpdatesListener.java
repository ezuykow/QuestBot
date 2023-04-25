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
import ru.coffeecoders.questbot.managers.ExceptionManager;
import ru.coffeecoders.questbot.managers.UpdateManager;
import ru.coffeecoders.questbot.messages.MessageSender;

import java.util.List;

@Service
public class QuestBotUpdatesListener implements UpdatesListener {

    Logger logger = LoggerFactory.getLogger(QuestBotUpdatesListener.class);

    private final TelegramBot bot;
    private final UpdateManager updateManager;
    private final ExceptionManager exceptionManager;
    private final MessageSender msgSender;

    private boolean startUp = true;

    public QuestBotUpdatesListener(UpdateManager updateManager, TelegramBot bot, ExceptionManager exceptionManager,
                                   MessageSender msgSender) {
        logger.warn("Starting bot...");

        this.exceptionManager = exceptionManager;
        this.updateManager = updateManager;
        this.msgSender = msgSender;
        this.bot = bot;
    }

    /**
     * @author anatoliy
     * @Redact: ezuykow
     */
    @PostConstruct
    public void init() {
        bot.execute(createSetMyCommands());
        bot.setUpdatesListener(this, createGetUpdates());
    }

    //-----------------API START-----------------

    /**
     * @author anatoliy
     * @Redact: ezuykow
     */
    @Override
    public int process(List<Update> updates) {
        if (!startUp) {
            performAllUpdates(updates);
        } else {
            logger.warn("Bot has been started!");
            msgSender.sendStartUp();
            startUp = false;
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void performAllUpdates(List<Update> updates) {
        updates.forEach(update -> {
            try {
                updateManager.performUpdate(update);
            } catch (Exception e) {
                exceptionManager.logException(e);
            }
        });
        msgSender.sendDeleteAllMessageToDelete();
    }

    /**
     * @author ezuykow
     */
    private SetMyCommands createSetMyCommands() {
        return new SetMyCommands(
                new BotCommand("regteam", "(Игрок) Создать команду"),
                new BotCommand("jointeam", "(Игрок) Вступить в команду"),
                new BotCommand("showgames", "(Админ) Показать все игры"),
                new BotCommand("showquestions", "(Админ) Показать все вопросы"),
                new BotCommand("newgame", "(Админ) Создать новую игру"),
                new BotCommand("deletechat", "(Админ) Удалить этот чат из системы"),
                new BotCommand("stopbot", "(Админ) Остановить бота"),
                new BotCommand("adminon", "(Владелец) Сделать текущий чат администраторским"),
                new BotCommand("adminoff", "(Владелец) Сделать текущий чат не администраторским"),
                new BotCommand("promote", "(Владелец) Назначить администратором бота"),
                new BotCommand("demote", "(Владелец) Назначить администратором бота")
        );
    }

    /**
     * @author ezuykow
     */
    private GetUpdates createGetUpdates() {
        return new GetUpdates().allowedUpdates(
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
    }
}

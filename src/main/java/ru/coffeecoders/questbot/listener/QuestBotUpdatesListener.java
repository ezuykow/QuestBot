package ru.coffeecoders.questbot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.logs.LogSender;
import ru.coffeecoders.questbot.managers.ExceptionManager;
import ru.coffeecoders.questbot.managers.UpdateManager;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.properties.PropertyService;

import java.util.List;

@Service
public class QuestBotUpdatesListener implements UpdatesListener {


    private final TelegramBot bot;
    private final UpdateManager updateManager;
    private final ExceptionManager exceptionManager;
    private final PropertyService propertyService;
    private final MessageSender msgSender;
    private final LogSender logger;

    private boolean startUp = true;

    public QuestBotUpdatesListener(UpdateManager updateManager, TelegramBot bot, ExceptionManager exceptionManager,
                                   PropertyService propertyService, MessageSender msgSender, LogSender logger) {
        this.propertyService = propertyService;
        this.logger = logger;
        this.logger.warn("Starting bot...");

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
        propertyService.fillProperties();
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
            performUpdates(updates, false);
        } else {
            logger.warn("Bot has been started!");
            msgSender.sendStartUp();
            startUp = false;
            performUpdates(updates, true);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void performUpdates(List<Update> updates, boolean afterSleep) {
        updates.forEach(update -> {
            try {
                if (afterSleep) {
                    updateManager.performUpdateAfterSleep(update);
                } else {
                    updateManager.performUpdate(update);
                }
            } catch (Exception e) {
                exceptionManager.logException(e);
            }
        });
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

package ru.coffeecoders.questbot.senders;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.ChatPermissions;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetChatMemberResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.entities.MessageToDelete;
import ru.coffeecoders.questbot.services.AdminChatService;
import ru.coffeecoders.questbot.services.GlobalChatService;
import ru.coffeecoders.questbot.services.MessageToDeleteService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ezuykow
 */
@Component
public class MessageSender {

    private final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    @Value("${messages.startUp}")
    private String startUpMsg;
    @Value("${messages.stopBot}")
    private String stopBotMsg;

    private final TelegramBot bot;
    private final MessageToDeleteService messageToDeleteService;
    private final AdminChatService adminChatService;
    private final GlobalChatService globalChatService;

    public MessageSender(TelegramBot bot, MessageToDeleteService messageToDeleteService,
                         AdminChatService adminChatService, GlobalChatService globalChatService) {
        this.bot = bot;
        this.messageToDeleteService = messageToDeleteService;
        this.adminChatService = adminChatService;
        this.globalChatService = globalChatService;
    }

    //-----------------API START-----------------

    /**
     * Отправляет сообщение с текстом {@code text} в чат c id {@code chatId}
     * @param chatId id чата, в который отправить сообщение
     * @param text текст сообщения
     * @author ezuykow
     */
    public void send(long chatId, String text) {
        checkResponse(bot.execute(new SendMessage(chatId, text)),
                String.format("Failed to send msg \"%s\" to chat %d! Error code: {}", text, chatId));
    }

    /**
     * Отправляет сообщение с текстом {@code text} в чат c id {@code chatId},
     * а также клавиатуру {@code kb}
     * @param chatId id чата, в который отправить сообщение
     * @param text текст сообщения
     * @param kb клавиатура, которую нужно отправить с сообщением
     * @author ezuykow
     */
    public void send(long chatId, String text, Keyboard kb) {
        checkResponse(bot.execute(new SendMessage(chatId, text).replyMarkup(kb)),
                String.format("Failed to send msg \"%s\" with kb to chat %d! Error code: {}", text, chatId));
    }

    /**
     * Отправляет сообщение с текстом {@code text} в чат c id {@code chatId}
     * ответом на сообщение с id {@code replyToMessageId}
     * @param chatId id чата, в который отправить сообщение
     * @param text текст сообщения
     * @param replyToMessageId id сообщения, на которое отвечаем
     * @return {@link SendResponse} ответ сервера на отправленное сообщение
     * @author ezuykow
     */
    public SendResponse send(long chatId, String text, int replyToMessageId) {
        SendResponse response = bot.execute(new SendMessage(chatId, text)
                .replyMarkup(new ForceReply(true)).replyToMessageId(replyToMessageId));
        checkResponse(response,
                String.format("Failed to send msg \"%s\" (reply to msg %d) to chat %d! Error code: {}",
                        text, replyToMessageId, chatId));
        return response;
    }

    /**
     * Отправляет сообщение с текстом {@code text} в чат c id {@code chatId},
     * а также клавиатуру {@code kb} ответом на сообщение с id {@code replyToMessageId}
     * @param chatId id чата, в который отправить сообщение
     * @param text текст сообщения
     * @param kb клавиатура, которую нужно отправить с сообщением
     * @param replyToMessageId id сообщения, на которое отвечаем
     * @author ezuykow
     */
    public void send(long chatId, String text, Keyboard kb, int replyToMessageId) {
        checkResponse(bot.execute(
                new SendMessage(chatId, text).replyMarkup(kb).replyToMessageId(replyToMessageId)),
                String.format("Failed to send msg \"%s\" (reply to msg %d) with kb to chat %d! Error code: {}",
                        text, replyToMessageId, chatId));
    }

    /**
     * Редактирует сообщение с id {@code msgId} в чате с id {@code chatId} - меняет текст на
     * {@code text}
     * @param chatId id чата
     * @param msgId id сообщения
     * @param text новый текст
     * @author ezuykow
     */
    public void edit(long chatId, int msgId, String text) {
        checkResponse(bot.execute(new EditMessageText(chatId, msgId, text)),
        String.format("Failed to edit msg %d in chat %d! Error {}", msgId, chatId));
    }

    /**
     * Редактирует сообщение с id {@code msgId} в чате с id {@code chatId} - меняет текст на
     * {@code text}, клавиатуру на {@code kb}, или приклепляет клавиатуру, если ее не было
     * @param chatId id чата
     * @param msgId id сообщения
     * @param text новый текст
     * @param kb клавиатура
     * @author ezuykow
     */
    public void edit(long chatId, int msgId, String text, InlineKeyboardMarkup kb) {
        checkResponse(bot.execute(new EditMessageText(chatId, msgId, text)
                        .replyMarkup(kb)),
                String.format("Failed to edit msg %d in chat %d! Error {}", msgId, chatId));
    }

    /**
     * Отправляет в ответ на {@link CallbackQuery} с {@code id = callbackId} ответ {@link AnswerCallbackQuery}
     * с текстом {@code text} в формате Alert (если {isAlert = true}) или нет
     * @param callbackId id калбака, на который отвечаем
     * @param text текст ответа
     * @param isAlert {@code true}, если отправить как Alert, {@code false} - если нет
     * @author ezuykow
     */
    public void sentToast(String callbackId, String text, boolean isAlert) {
        checkResponse(bot.execute(
                        new AnswerCallbackQuery(callbackId)
                                .text(text)
                                .showAlert(isAlert)
                                .cacheTime(5)
                ),
                String.format("Failed to answer callback %s! Error {}", callbackId));
    }

    /**
     * Удаляет сообщение с id {@code msgId} в чате с id {@code chatId}
     * @param chatId id чата
     * @param msgId id сообщения
     * @author ezuykow
     */
    public void sendDelete(long chatId, int msgId) {
        checkResponse(bot.execute(new DeleteMessage(chatId, msgId)),
                String.format("Failed to delete msg %d in chat %d! Error {}", msgId, chatId));
    }

    /**
     * Вызывает {@link MessageSender#sendDelete} для всех сообщений,
     * находящихся в таблице msg_to_delete в БД с полем active = false
     * @author ezuykow
     */
    public void sendDeleteAllMessageToDelete() {
        List<MessageToDelete> mtds = messageToDeleteService.findAll().stream().
                filter(m -> !m.isActive())
                .toList();
        mtds.forEach(m -> sendDelete(m.getChatId(), m.getMsgId()));
        messageToDeleteService.deleteAll(mtds);
    }

    /**
     * Отправляет {@link LeaveChat}, т.е. бот покинет чат с этим {@code chatId}
     * @param chatId id чата
     * @author ezuykow
     */
    public void sendLeaveChat(long chatId) {
        checkResponse(bot.execute(new LeaveChat(chatId)),
                String.format("Failed to leave chat %d! Error {}", chatId));
    }

    /**
     * Если в чате с {@code id = chatId} есть {@link User} с {@code id = userId}, то возвращает его
     * @param chatId id чата
     * @param userId id пользователя
     * @return {@link User} с {@code id = userId}
     * @author ezuykow
     */
    public User getChatMember(long chatId, long userId) {
        GetChatMemberResponse response = bot.execute(
                new GetChatMember(chatId, userId)
        );
        if (response.isOk()) {
            return response.chatMember().user();
        } else {
            logger.error("GetChatMember failed! Error code: {}", response.errorCode());
            return null;
        }
    }

    /**
     * Отправляет {@link RestrictChatMember} c {@code chatId, userId, permissions}
     * @param chatId id чата
     * @param userId id пользователя
     * @param permissions {@link ChatPermissions} - разрешения
     * @author ezuykow
     */
    public void sendRestrictChatMember(long chatId, long userId, ChatPermissions permissions) {
        checkResponse(bot.execute(new RestrictChatMember(chatId, userId, permissions)),
                String.format("Failed to restrict chat member %d! Error {}", userId));
    }

    /**
     * Отправляет сообщение о запуске бота во все чаты, добавленные в систему
     * @author ezuykow
     */
    public void sendStartUp() {
        getAllChatIds().forEach(id -> send(id, startUpMsg));
    }

    /**
     * Отправляет сообщение об остановке бота во все чаты, добавленные в систему
     * @author ezuykow
     */
    public void sendStopBot() {
        getAllChatIds().forEach(id -> send(id, stopBotMsg));
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private List<Long> getAllChatIds() {
        List<Long> chatIds = new ArrayList<>(
                adminChatService.findAll().stream().map(AdminChat::getTgAdminChatId).toList());
        chatIds.addAll(globalChatService.findAll().stream().map(GlobalChat::getTgChatId).toList());
        return chatIds;
    }

    /**
     * @author ezuykow
     */
    private void checkResponse(BaseResponse response, String error) {
        if (!response.isOk()) {
            logger.warn(error, response.errorCode());
        }
    }
}

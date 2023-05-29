package ru.coffeecoders.questbot.actions.newgame.utils;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.keyboards.QuestionsGroupsKeyboard;
import ru.coffeecoders.questbot.messages.MessageBuilder;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.services.QuestionGroupService;

/**
 * @author ezuykow
 */
@Component
public class NewGameRequests {

    private final QuestionGroupService questionGroupService;
    private final MessageSender msgSender;
    private final Messages messages;
    private final MessageBuilder messageBuilder;

    public NewGameRequests(QuestionGroupService questionGroupService, MessageSender msgSender,
                           Messages messages, MessageBuilder messageBuilder) {
        this.questionGroupService = questionGroupService;
        this.msgSender = msgSender;
        this.messages = messages;
        this.messageBuilder = messageBuilder;
    }

    //-----------------API START-----------------

    /**
     * Отправляет в чат с {@code chatId} сообщение с запросом названия новой игры
     * @param chatId id чата
     * @author ezuykow
     */
    public void requestNewGameName(long chatId) {
        msgSender.send(chatId,
                messageBuilder.build(messages.requestNewGameName(), chatId));
    }

    /**
     * Изменяет сообщение с {@code requestMsgId} в чате с {@code chatId}  на запрос групп вопросов для новой игры
     * @param chatId id чата
     * @param requestMsgId id изменяемого сообщения
     * @author ezuykow
     */
    public void requestQuestionGroups(long chatId, int requestMsgId) {
        msgSender.edit(chatId, requestMsgId,
                messageBuilder.build(messages.requestQuestionsGroups(), chatId),
                QuestionsGroupsKeyboard.createKeyboard(questionGroupService.findAll())
        );
    }

    /**
     * Изменяет сообщение с {@code msgIdToEdit} в чате с {@code chatId}  на запрос максимального
     * количества вопросов для новой игры
     * @param chatId id чата
     * @param msgIdToEdit id изменяемого сообщения
     * @author ezuykow
     */
    public void requestMaxQuestionsCount(long chatId, int msgIdToEdit) {
        msgSender.edit(chatId, msgIdToEdit, messageBuilder.build(messages.requestMaxQuestionsCount(), chatId));
    }

    /**
     * Изменяет сообщение с {@code msgIdToEdit} в чате с {@code chatId}  на запрос начального
     * количества вопросов для новой игры
     * @param chatId id чата
     * @param msgIdToEdit id изменяемого сообщения
     * @author ezuykow
     */
    public void requestStartCountTasks(long chatId, int msgIdToEdit) {
        msgSender.edit(chatId, msgIdToEdit, messageBuilder.build(messages.requestStartCountTasks(), chatId));
    }

    /**
     * Изменяет сообщение с {@code msgIdToEdit} в чате с {@code chatId}  на запрос максимального
     * количества вопросов на которое нужно ответить для досрочной победы для новой игры
     * @param chatId id чата
     * @param msgIdToEdit id изменяемого сообщения
     * @author ezuykow
     */
    public void requestMaxPerformedQuestionCount(long chatId, int msgIdToEdit) {
        msgSender.edit(chatId, msgIdToEdit, messageBuilder.build(messages.requestMaxPerformedQuestionCount(), chatId));
    }

    /**
     * Изменяет сообщение с {@code msgIdToEdit} в чате с {@code chatId}  на запрос минимального
     * количества активных вопросов для новой игры
     * @param chatId id чата
     * @param msgIdToEdit id изменяемого сообщения
     * @author ezuykow
     */
    public void requestMinQuestionsCountInGame(long chatId, int msgIdToEdit) {
        msgSender.edit(chatId, msgIdToEdit, messageBuilder.build(messages.requestMinQuestionsCountInGame(), chatId));
    }

    /**
     * Изменяет сообщение с {@code msgIdToEdit} в чате с {@code chatId}  на запрос
     * количества вопросов, которое нужно добавлять при достижении минимального количества активных вопросов для новой игры
     * @param chatId id чата
     * @param msgIdToEdit id изменяемого сообщения
     * @author ezuykow
     */
    public void requestQuestionsCountToAdd(long chatId, int msgIdToEdit) {
        msgSender.edit(chatId, msgIdToEdit, messageBuilder.build(messages.requestQuestionsCountToAdd(), chatId));
    }

    /**
     * Изменяет сообщение с {@code msgIdToEdit} в чате с {@code chatId}  на запрос
     * времени проведения в минутах для новой игры
     * @param chatId id чата
     * @param msgIdToEdit id изменяемого сообщения
     * @author ezuykow
     */
    public void requestMaxTimeMinutes(long chatId, int msgIdToEdit) {
        msgSender.edit(chatId, msgIdToEdit, messageBuilder.build(messages.requestMaxTimeMinutes(), chatId));
    }

    public void requestAdditionWithTask(long chatId, int msgIdToEdit) {
        msgSender.edit(chatId, msgIdToEdit, messageBuilder.build(messages.requestAdditionWithTask(), chatId));
    }

    //-----------------API END-----------------

}

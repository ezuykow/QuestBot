package ru.coffeecoders.questbot.actions.newgame.utils;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.NewGameCreatingState;
import ru.coffeecoders.questbot.keyboards.QuestionsGroupsKeyboard;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.services.QuestionGroupService;

/**
 * @author ezuykow
 */
@Component
public class NewGameRequests {

    private final QuestionGroupService questionGroupService;
    private final NewGameUtils utils;
    private final MessageSender msgSender;
    private final Environment env;

    public NewGameRequests(QuestionGroupService questionGroupService, NewGameUtils utils, MessageSender msgSender,
                           Environment env) {
        this.questionGroupService = questionGroupService;
        this.utils = utils;
        this.msgSender = msgSender;
        this.env = env;
    }

    //-----------------API START-----------------

    /**
     * Отправляет в чат с {@code chatId} сообщение с запросом названия новой игры
     * @param chatId id чата
     * @author ezuykow
     */
    public void requestNewGameName(long chatId) {
        msgSender.send(chatId, env.getProperty("messages.game.requestNewGameName"));
    }

    /**
     * Изменяет сообщение с {@code requestMsgId} в чате с {@code chatId}  на запрос групп вопросов для новой игры
     * @param gameName название новой игры
     * @param chatId id чата
     * @param requestMsgId id изменяемого сообщения
     * @author ezuykow
     */
    public void requestQuestionGroups(String gameName, long chatId, int requestMsgId) {
        msgSender.edit(chatId, requestMsgId,
                String.format(
                        env.getProperty("messages.game.requestQuestionsGroups", "Error"), gameName),
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
        NewGameCreatingState state = utils.getNewGameCreatingState(chatId);
        msgSender.edit(chatId, msgIdToEdit,
                utils.createTextFromStateFields(
                        env.getProperty("messages.game.requestMaxQuestionsCount"), 2, state));
    }

    /**
     * Изменяет сообщение с {@code msgIdToEdit} в чате с {@code chatId}  на запрос начального
     * количества вопросов для новой игры
     * @param chatId id чата
     * @param msgIdToEdit id изменяемого сообщения
     * @param state {@link NewGameCreatingState} - состояние создания новой игры
     * @author ezuykow
     */
    public void requestStartCountTasks(long chatId, int msgIdToEdit,
                                        NewGameCreatingState state) {
        msgSender.edit(chatId, msgIdToEdit,
                utils.createTextFromStateFields(
                        env.getProperty("messages.game.requestStartCountTasks"), 3, state));
    }

    /**
     * Изменяет сообщение с {@code msgIdToEdit} в чате с {@code chatId}  на запрос максимального
     * количества вопросов на которое нужно ответить для досрочной победы для новой игры
     * @param chatId id чата
     * @param msgIdToEdit id изменяемого сообщения
     * @param state {@link NewGameCreatingState} - состояние создания новой игры
     * @author ezuykow
     */
    public void requestMaxPerformedQuestionCount(long chatId, int msgIdToEdit, NewGameCreatingState state) {
        msgSender.edit(chatId, msgIdToEdit,
                utils.createTextFromStateFields(
                        env.getProperty("messages.game.requestMaxPerformedQuestionCount"), 4, state));
    }

    /**
     * Изменяет сообщение с {@code msgIdToEdit} в чате с {@code chatId}  на запрос минимального
     * количества активных вопросов для новой игры
     * @param chatId id чата
     * @param msgIdToEdit id изменяемого сообщения
     * @param state {@link NewGameCreatingState} - состояние создания новой игры
     * @author ezuykow
     */
    public void requestMinQuestionsCountInGame(long chatId, int msgIdToEdit, NewGameCreatingState state) {
        msgSender.edit(chatId, msgIdToEdit,
                utils.createTextFromStateFields(
                        env.getProperty("messages.game.requestMinQuestionsCountInGame"), 5, state));
    }

    /**
     * Изменяет сообщение с {@code msgIdToEdit} в чате с {@code chatId}  на запрос
     * количества вопросов, которое нужно добавлять при достижении минимального количества активных вопросов для новой игры
     * @param chatId id чата
     * @param msgIdToEdit id изменяемого сообщения
     * @param state {@link NewGameCreatingState} - состояние создания новой игры
     * @author ezuykow
     */
    public void requestQuestionsCountToAdd(long chatId, int msgIdToEdit, NewGameCreatingState state) {
        msgSender.edit(chatId, msgIdToEdit,
                utils.createTextFromStateFields(
                        env.getProperty("messages.game.requestQuestionsCountToAdd"), 6, state));
    }

    /**
     * Изменяет сообщение с {@code msgIdToEdit} в чате с {@code chatId}  на запрос
     * времени проведения в минутах для новой игры
     * @param chatId id чата
     * @param msgIdToEdit id изменяемого сообщения
     * @param state {@link NewGameCreatingState} - состояние создания новой игры
     * @author ezuykow
     */
    public void requestMaxTimeMinutes(long chatId, int msgIdToEdit, NewGameCreatingState state) {
        msgSender.edit(chatId, msgIdToEdit,
                utils.createTextFromStateFields(
                        env.getProperty("messages.game.requestMaxTimeMinutes"), 7, state));
    }

    //-----------------API END-----------------

}

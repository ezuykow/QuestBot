package ru.coffeecoders.questbot.actions.newgame;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.actions.newgame.utils.NewGameRequests;
import ru.coffeecoders.questbot.actions.newgame.utils.NewGameUtils;
import ru.coffeecoders.questbot.entities.NewGameCreatingState;
import ru.coffeecoders.questbot.managers.BlockingManager;
import ru.coffeecoders.questbot.managers.RestrictingManager;
import ru.coffeecoders.questbot.messages.MessageBuilder;
import ru.coffeecoders.questbot.messages.MessageSender;
import ru.coffeecoders.questbot.messages.Messages;
import ru.coffeecoders.questbot.services.NewGameCreatingStateService;
import ru.coffeecoders.questbot.validators.GameValidator;
import ru.coffeecoders.questbot.validators.QuestionsValidator;

/**
 * @author ezuykow
 */
@Component
public class NewGameActions {

    private final NewGameCreatingStateService newGameCreatingStateService;
    private final QuestionsValidator questionsValidator;
    private final GameValidator gameValidator;
    private final NewGameRequests requests;
    private final NewGameUtils utils;
    private final BlockingManager blockingManager;
    private final RestrictingManager restrictingManager;
    private final MessageSender msgSender;
    private final Messages messages;
    private final MessageBuilder messageBuilder;

    public NewGameActions(NewGameCreatingStateService newGameCreatingStateService,
                          QuestionsValidator questionsValidator, GameValidator gameValidator, NewGameRequests requests,
                          NewGameUtils utils, BlockingManager blockingManager, RestrictingManager restrictingManager,
                          MessageSender msgSender, Messages messages, MessageBuilder messageBuilder) {
        this.newGameCreatingStateService = newGameCreatingStateService;
        this.questionsValidator = questionsValidator;
        this.gameValidator = gameValidator;
        this.requests = requests;
        this.utils = utils;
        this.blockingManager = blockingManager;
        this.restrictingManager = restrictingManager;
        this.msgSender = msgSender;
        this.messages = messages;
        this.messageBuilder = messageBuilder;
    }

    //-----------------API START-----------------

    /**
     * Создает {@link NewGameCreatingState} для чата с {@code chatId} и вызывает
     * {@link NewGameRequests#requestNewGameName}
     * @param chatId id чата
     * @author ezuykow
     */
    public void createNewGameCreatingState(long chatId) {
        newGameCreatingStateService.save(
                new NewGameCreatingState(chatId)
        );
        requests.requestNewGameName(chatId);
    }

    /**
     * Проверяет, что такое {@code gameName} еще не используется и вызывает
     * {@link NewGameActions#addGameNameAndRequestNext}, в противном случае повторяет запрос
     * @param chatId id чата
     * @param state {@link NewGameCreatingState} - состояние создания новой игры
     * @param gameName введенное название новой игры
     * @param answerMsgId id сообщения с {@code gameName}
     * @author ezuykow
     */
    public void validateGameNameToStateAndRequestNextPart(long chatId, NewGameCreatingState state,
                                                          String gameName, int answerMsgId) {
        msgSender.sendDelete(chatId, answerMsgId);
        utils.calcRequestMsgId(state, answerMsgId);
        int requestMsgId = state.getRequestMsgId();
        if (!gameValidator.isGameNameAlreadyTaken(gameName)) {
            addGameNameAndRequestNext(chatId, state, gameName, requestMsgId);
        } else {
            state.setGameName(gameName);
            msgSender.edit(chatId, requestMsgId,
                    messageBuilder.build(messages.nameAlreadyTaken() + messages.requestNewGameName(), chatId));
        }
    }

    /**
     * Добавляет выбранную группу вопросов к состоянию создания новой игры и повторяет запрос
     * @param chatId id чата
     * @param msgId id сообщения с запросом группы
     * @param questionGroupId id выбранной группы
     * @author ezuykow
     */
    public void addSelectedQuestionGroupAndRefreshMsg(long chatId, int msgId, int questionGroupId) {
        InlineKeyboardMarkup kb = utils.addQuestionGroupAndGetKeyboard(chatId, questionGroupId);
        msgSender.edit(chatId, msgId,
                messageBuilder.build(messages.addedQuestionGroup(), chatId), kb);
    }

    /**
     * Если хотя бы одна группа вопросов была выбрана, то вызывает {@link NewGameRequests#requestMaxQuestionsCount},
     * в противном случае игнорирует
     * @param chatId id чата
     * @param msgId id сообщения с запросом группы
     * @author ezuykow
     */
    public void stopSelectingQuestionsGroupsAndRequestNextPart(long chatId, int msgId) {
        if (utils.getNewGameCreatingState(chatId).getGroupsIds() != null) {
            requests.requestMaxQuestionsCount(chatId, msgId);
        }
    }

    /**
     * Проверяет, что максимальное количество вопросов введено корректно, больше 0, но не больше 
     * общего количества вопросов и вызывает {@link NewGameActions#addMaxQuestionsCountAndRequestNext},
     * в противном случае повторяет запрос
     * @param chatId id чата
     * @param state {@link NewGameCreatingState} - состояние создания новой игры
     * @param text текст с ответом
     * @param msgId id сообщения с ответом
     * @author ezuykow
     */
    public void validateMaxQuestionsCountToStateAndRequestNextPart(long chatId, NewGameCreatingState state,
                                                                   String text, int msgId) {
        Integer maxQuestionCount = utils.parseTextToInteger(text);
        msgSender.sendDelete(chatId, msgId);
        int requestMsgId = state.getRequestMsgId();
        if ((maxQuestionCount != null && maxQuestionCount > 0)
                && questionsValidator.isRequestedQuestionCountNotMoreThanWeHaveByGroups(
                        maxQuestionCount, state.getGroupsIds()))
        {
                addMaxQuestionsCountAndRequestNext(chatId, state, maxQuestionCount, requestMsgId);
        } else {
            String msgHat = ((maxQuestionCount != null) && (maxQuestionCount > 0))
                    ? messages.invalidQuestionCount()
                    : messages.invalidNumber();
            msgSender.edit(chatId, msgId,
                    messageBuilder.build(msgHat + messages.requestMaxQuestionsCount(), chatId));
        }
    }

    /**
     * Проверяет, что стартовое количество вопросов введено корректно, больше 0, но не больше
     * максимального количества вопросов и вызывает {@link NewGameActions#addStartCountTasksAndRequestNext},
     * в противном случае повторяет запрос
     * @param chatId id чата
     * @param state {@link NewGameCreatingState} - состояние создания новой игры
     * @param text текст с ответом
     * @param msgId id сообщения с ответом
     * @author ezuykow
     */
    public void validateStartCountTaskToStateAndRequestNextPart(long chatId, NewGameCreatingState state,
                                                                String text, int msgId) {
        Integer startCountTask = utils.parseTextToInteger(text);
        msgSender.sendDelete(chatId, msgId);
        int requestMsgId = state.getRequestMsgId();
        int maxQuestionCount = state.getMaxQuestionsCount();
        if ((startCountTask != null && startCountTask > 0) && (startCountTask <= maxQuestionCount)) {
            addStartCountTasksAndRequestNext(chatId, state, startCountTask, requestMsgId);
        } else {
            String msgHat = ((startCountTask != null) && (startCountTask > 0))
                    ? messages.startQMoreMaxQ()
                    : messages.invalidNumber();
            msgSender.edit(chatId, msgId,
                    messageBuilder.build(msgHat + messages.requestStartCountTasks(), chatId));
        }
    }

    /**
     * Проверяет, что максимальное количество вопросов, на которое нужно ответить для досрочной победы
     * введено корректно, больше 0, но не больше
     * максимального количества вопросов и вызывает {@link NewGameActions#addMaxPerformedAndRequestNext},
     * в противном случае повторяет запрос
     * @param chatId id чата
     * @param state {@link NewGameCreatingState} - состояние создания новой игры
     * @param text текст с ответом
     * @param msgId id сообщения с ответом
     * @author ezuykow
     */
    public void validateMaxPerformedQuestionsCountToStateAndRequestNextPart(long chatId, NewGameCreatingState state,
                                                                            String text, int msgId) {
        Integer maxPerformedQuestionsCount = utils.parseTextToInteger(text);
        msgSender.sendDelete(chatId, msgId);
        int requestMsgId = state.getRequestMsgId();
        if ((maxPerformedQuestionsCount != null && maxPerformedQuestionsCount > 0)
                && (maxPerformedQuestionsCount <= state.getMaxQuestionsCount()))
        {
            addMaxPerformedAndRequestNext(chatId, state, maxPerformedQuestionsCount, requestMsgId);
        } else {
            String msgHat = ((maxPerformedQuestionsCount != null) && (maxPerformedQuestionsCount > 0))
                    ? messages.maxPerformedQMoreMaxQ()
                    : messages.invalidNumber();
            msgSender.edit(chatId, msgId,
                    messageBuilder.build(msgHat + messages.requestMaxPerformedQuestionCount(), chatId));
        }
    }

    /**
     * Проверяет, что минимальное количество активных вопросов
     * введено корректно и больше -1
     * и вызывает {@link NewGameActions#addMinQuestionAndRequestNext},
     * в противном случае повторяет запрос
     * @param chatId id чата
     * @param state {@link NewGameCreatingState} - состояние создания новой игры
     * @param text текст с ответом
     * @param msgId id сообщения с ответом
     * @author ezuykow
     */
    public void validateMinQuestionsCountInGameAndRequestNextPart(long chatId, NewGameCreatingState state,
                                                                  String text, int msgId) {
        Integer minQuestionsInGame = utils.parseTextToInteger(text);
        msgSender.sendDelete(chatId, msgId);
        int requestMsgId = state.getRequestMsgId();
        if (minQuestionsInGame != null && minQuestionsInGame >= 0) {
            addMinQuestionAndRequestNext(state, chatId, minQuestionsInGame, requestMsgId);
        } else {
            msgSender.edit(chatId, msgId,
                    messageBuilder.build(messages.invalidNumber() + messages.requestMinQuestionsCountInGame(), chatId));
        }
    }

    /**
     * Проверяет, что количество вопросов, которое нужно добавлять при достижении порога активных
     * введено корректно и больше -1
     * и вызывает {@link NewGameActions#addQuestionsToAddAndPerformNext},
     * в противном случае повторяет запрос
     * @param chatId id чата
     * @param state {@link NewGameCreatingState} - состояние создания новой игры
     * @param text текст с ответом
     * @param msgId id сообщения с ответом
     * @author ezuykow
     */
    public void validateQuestionsCountToAddAndRequestNextPart(long chatId, NewGameCreatingState state,
                                                              String text, int msgId) {
        Integer questionsToAdd = utils.parseTextToInteger(text);
        msgSender.sendDelete(chatId, msgId);
        int requestMsgId = state.getRequestMsgId();
        if (questionsToAdd != null && questionsToAdd >= 0) {
            addQuestionsToAddAndPerformNext(state, chatId, questionsToAdd, requestMsgId);
        } else {
            msgSender.edit(chatId, msgId,
                    messageBuilder.build(messages.invalidNumber() + messages.requestQuestionsCountToAdd(), chatId));
        }
    }

    /**
     * Проверяет, что время проведения игры в минутах
     * введено корректно и больше 0
     * и вызывает {@link NewGameActions#addTimeAndRequestAddition},
     * в противном случае повторяет запрос
     * @param chatId id чата
     * @param state {@link NewGameCreatingState} - состояние создания новой игры
     * @param text текст с ответом
     * @param msgId id сообщения с ответом
     * @author ezuykow
     */
    public void validateMaxTimeMinutesAndRequestAddition(long chatId, NewGameCreatingState state,
                                                         String text, int msgId) {
        Integer minutes = utils.parseTextToInteger(text);
        msgSender.sendDelete(chatId, msgId);
        int requestMsgId = state.getRequestMsgId();
        if (minutes != null && minutes > 0) {
            addTimeAndRequestAddition(state, minutes, chatId, requestMsgId);
        } else {
            msgSender.edit(chatId, msgId,
                    messageBuilder.build(messages.invalidNumber() + messages.requestMaxTimeMinutes(), chatId));
        }
    }

    public void validateAdditionWithTaskAndSaveNewGame(long chatId, NewGameCreatingState state, String text, int msgId) {
        Integer i = utils.parseTextToInteger(text);
        msgSender.sendDelete(chatId, msgId);
        int requestMsgId = state.getRequestMsgId();
        switch (i) {
            case 1, 0 -> addAdditionWithTaskAndSaveNewGame(state, i, chatId, requestMsgId);
            default ->
                    msgSender.edit(chatId, msgId,
                            messageBuilder.build(messages.invalidNumber() + messages.requestAdditionWithTask(), chatId));
        }
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void addGameNameAndRequestNext(long chatId, NewGameCreatingState state, String gameName, int requestMsgId) {
        state.setGameName(gameName);
        newGameCreatingStateService.save(state);
        requests.requestQuestionGroups(chatId, requestMsgId);
    }

    /**
     * @author ezuykow
     */
    private void addMaxQuestionsCountAndRequestNext(long chatId, NewGameCreatingState state,
                                                    Integer maxQuestionCount, int requestMsgId) {
        state.setMaxQuestionsCount(maxQuestionCount);
        newGameCreatingStateService.save(state);
        requests.requestStartCountTasks(chatId, requestMsgId);
    }

    /**
     * @author ezuykow
     */
    private void addStartCountTasksAndRequestNext(long chatId, NewGameCreatingState state,
                                                  Integer startCountTask, int requestMsgId) {
        state.setStartCountTasks(startCountTask);
        newGameCreatingStateService.save(state);
        requests.requestMaxPerformedQuestionCount(chatId, requestMsgId);
    }

    /**
     * @author ezuykow
     */
    private void addMaxPerformedAndRequestNext(long chatId, NewGameCreatingState state,
                                               Integer maxPerformedQuestionsCount, int requestMsgId) {
        state.setMaxPerformedQuestionsCount(maxPerformedQuestionsCount);
        newGameCreatingStateService.save(state);
        requests.requestMinQuestionsCountInGame(chatId, requestMsgId);
    }

    /**
     * @author ezuykow
     */
    private void addMinQuestionAndRequestNext(NewGameCreatingState state, long chatId,
                                              Integer minQuestionsInGame, int requestMsgId) {
        state.setMinQuestionsCountInGame(minQuestionsInGame);
        newGameCreatingStateService.save(state);
        requests.requestQuestionsCountToAdd(chatId, requestMsgId);
    }

    /**
     * @author ezuykow
     */
    private void addQuestionsToAddAndPerformNext(NewGameCreatingState state, long chatId,
                                                 Integer questionsToAdd, int requestMsgId) {
        state.setQuestionsCountToAdd(questionsToAdd);
        newGameCreatingStateService.save(state);
        requests.requestMaxTimeMinutes(chatId, requestMsgId);
    }

    /**
     * @author ezuykow
     */
    private void addTimeAndRequestAddition(NewGameCreatingState state, Integer minutes, long chatId, int requestMsgId) {
        state.setMaxTimeMinutes(minutes);
        newGameCreatingStateService.save(state);
        requests.requestAdditionWithTask(chatId, requestMsgId);
    }

    private void addAdditionWithTaskAndSaveNewGame(NewGameCreatingState state, Integer i, long chatId, int requestMsgId) {
        state.setAdditionWithTask(i == 0);
        utils.saveNewGame(state);
        unblockAndUnrestrictChat(chatId);
        msgSender.edit(chatId, requestMsgId,
                messageBuilder.build(messages.gameAdded(), chatId));
        newGameCreatingStateService.delete(state);
    }

    /**
     * @author ezuykow
     */
    private void unblockAndUnrestrictChat(long chatId) {
        blockingManager.unblockAdminChat(chatId, messages.endGameCreating());
        restrictingManager.unRestrictMembers(chatId);
    }
}

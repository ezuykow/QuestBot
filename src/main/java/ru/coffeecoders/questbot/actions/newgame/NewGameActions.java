package ru.coffeecoders.questbot.actions.newgame;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.actions.newgame.utils.NewGameRequests;
import ru.coffeecoders.questbot.actions.newgame.utils.NewGameUtils;
import ru.coffeecoders.questbot.entities.NewGameCreatingState;
import ru.coffeecoders.questbot.managers.BlockingManager;
import ru.coffeecoders.questbot.managers.RestrictingManager;
import ru.coffeecoders.questbot.senders.MessageSender;
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
    private final Environment env;

    public NewGameActions(NewGameCreatingStateService newGameCreatingStateService,
                          QuestionsValidator questionsValidator, GameValidator gameValidator, NewGameRequests requests,
                          NewGameUtils utils, BlockingManager blockingManager, RestrictingManager restrictingManager,
                          MessageSender msgSender, Environment env) {
        this.newGameCreatingStateService = newGameCreatingStateService;
        this.questionsValidator = questionsValidator;
        this.gameValidator = gameValidator;
        this.requests = requests;
        this.utils = utils;
        this.blockingManager = blockingManager;
        this.restrictingManager = restrictingManager;
        this.msgSender = msgSender;
        this.env = env;
    }

    //-----------------API START-----------------

    public void createNewGameCreatingState(long chatId) {
        newGameCreatingStateService.save(
                new NewGameCreatingState(chatId)
        );
        requests.requestNewGameName(chatId);
    }

    public void validateGameNameToStateAndRequestNextPart(long chatId, NewGameCreatingState state,
                                                          String gameName, int answerMsgId) {
        msgSender.sendDelete(chatId, answerMsgId);
        utils.calcRequestMsgId(state, answerMsgId);
        int requestMsgId = state.getRequestMsgId();
        if (!gameValidator.isGameNameAlreadyTaken(gameName)) {
            addGameNameAndRequestNext(chatId, state, gameName, requestMsgId);
        } else {
            state.setGameName(gameName);
            switchMsg1(chatId, requestMsgId, state, env.getProperty("messages.game.nameAlreadyTaken"));
        }
    }

    public void addSelectedQuestionGroupAndRefreshMsg(long chatId, int msgId, int questionGroupId) {
        InlineKeyboardMarkup kb = utils.addQuestionGroupAndGetKeyboard(chatId, questionGroupId);
        utils.switchMsg(2, chatId, msgId, utils.getNewGameCreatingState(chatId),
                env.getProperty("messages.game.addedQuestionGroup"), kb);
    }

    public void stopSelectingQuestionsGroupsAndRequestNextPart(long chatId, int msgId) {
        if (utils.getNewGameCreatingState(chatId).getGroupsIds() != null) {
            requests.requestMaxQuestionsCount(chatId, msgId);
        }
    }

    public void validateMaxQuestionsCountToStateAndRequestNextPart(long chatId, NewGameCreatingState state,
                                                                   String text, int msgId) {
        Integer maxQuestionCount = utils.parseTextToInteger(text);
        msgSender.sendDelete(chatId, msgId);
        int requestMsgId = state.getRequestMsgId();
        if (maxQuestionCount != null && maxQuestionCount > 0) {
            if (questionsValidator.isRequestedQuestionCountNotMoreThanWeHaveByGroups(
                    maxQuestionCount, state.getGroupsIds())) {
                addMaxQuestionsCountAndRequestNext(chatId, state, maxQuestionCount, requestMsgId);
            } else {
                switchMsg3(chatId, requestMsgId, state, env.getProperty("messages.game.invalidQuestionCount"));
            }
        } else {
            switchMsg3(chatId, requestMsgId, state, env.getProperty("messages.game.invalidNumber"));
        }
    }

    public void validateStartCountTaskToStateAndRequestNextPart(long chatId, NewGameCreatingState state,
                                                                String text, int msgId) {
        Integer startCountTask = utils.parseTextToInteger(text);
        msgSender.sendDelete(chatId, msgId);
        int requestMsgId = state.getRequestMsgId();
        int maxQuestionCount = state.getMaxQuestionsCount();
        if (startCountTask != null && startCountTask > 0) {
            if (startCountTask <= maxQuestionCount) {
                addStartCountTasksAndRequestNext(chatId, state, startCountTask, requestMsgId);
            } else {
                switchMsg4(chatId, requestMsgId, state, env.getProperty("messages.game.startQMoreMaxQ"));
            }
        } else {
            switchMsg4(chatId, requestMsgId, state, env.getProperty("messages.game.invalidNumber"));
        }
    }

    public void validateMaxPerformedQuestionsCountToStateAndRequestNextPart(long chatId, NewGameCreatingState state,
                                                                            String text, int msgId) {
        Integer maxPerformedQuestionsCount = utils.parseTextToInteger(text);
        msgSender.sendDelete(chatId, msgId);
        int requestMsgId = state.getRequestMsgId();
        if (maxPerformedQuestionsCount != null && maxPerformedQuestionsCount > 0) {
            if (maxPerformedQuestionsCount <= state.getMaxQuestionsCount()) {
                addMaxPerformedAndRequestNext(chatId, state, maxPerformedQuestionsCount, requestMsgId);
            } else {
                switchMsg5(chatId, requestMsgId, state, env.getProperty("messages.game.maxPerformedQMoreMaxQ"));
            }
        } else {
            switchMsg5(chatId, requestMsgId, state, env.getProperty("messages.game.invalidNumber"));
        }
    }

    public void validateMinQuestionsCountInGameAndRequestNextPart(long chatId, NewGameCreatingState state,
                                                                  String text, int msgId) {
        Integer minQuestionsInGame = utils.parseTextToInteger(text);
        msgSender.sendDelete(chatId, msgId);
        int requestMsgId = state.getRequestMsgId();
        if (minQuestionsInGame != null && minQuestionsInGame >= 0) {
            addMinQuestionAndRequestNext(state, chatId, minQuestionsInGame, requestMsgId);
        } else {
            switchMsg6(chatId, requestMsgId, state, env.getProperty("messages.game.invalidNumber"));
        }
    }

    public void validateQuestionsCountToAddAndRequestNextPart(long chatId, NewGameCreatingState state,
                                                              String text, int msgId) {
        Integer questionsToAdd = utils.parseTextToInteger(text);
        msgSender.sendDelete(chatId, msgId);
        int requestMsgId = state.getRequestMsgId();
        if (questionsToAdd != null && questionsToAdd >= 0) {
            addQuestionsToAddAndPerformNext(state, chatId, questionsToAdd, requestMsgId);
        } else {
            switchMsg7(chatId, requestMsgId, state, env.getProperty("messages.game.invalidNumber"));
        }
    }

    public void validateMaxTimeMinutesToStateAmdSaveNewGame(long chatId, NewGameCreatingState state,
                                                            String text, int msgId) {
        Integer minutes = utils.parseTextToInteger(text);
        msgSender.sendDelete(chatId, msgId);
        int requestMsgId = state.getRequestMsgId();
        if (minutes != null && minutes > 0) {
            addTimeAndSaveGame(state, minutes, chatId, requestMsgId);
        } else {
            switchMsg9(chatId, requestMsgId, state, env.getProperty("messages.game.invalidNumber"));
        }
    }

    //-----------------API END-----------------

    private void addGameNameAndRequestNext(long chatId, NewGameCreatingState state, String gameName, int requestMsgId) {
        state.setGameName(gameName);
        newGameCreatingStateService.save(state);
        requests.requestQuestionGroups(gameName, chatId, requestMsgId);
    }

    private void switchMsg1(long chatId, int requestMsgId, NewGameCreatingState state, String prop) {
        utils.switchMsg(1, chatId, requestMsgId, state, prop, null);
    }

    private void addMaxQuestionsCountAndRequestNext(long chatId, NewGameCreatingState state,
                                                    Integer maxQuestionCount, int requestMsgId) {
        state.setMaxQuestionsCount(maxQuestionCount);
        newGameCreatingStateService.save(state);
        requests.requestStartCountTasks(chatId, requestMsgId, state);
    }

    private void switchMsg3(long chatId, int requestMsgId, NewGameCreatingState state, String prop) {
        utils.switchMsg(3, chatId, requestMsgId, state, prop, null);
    }

    private void addStartCountTasksAndRequestNext(long chatId, NewGameCreatingState state,
                                                  Integer startCountTask, int requestMsgId) {
        state.setStartCountTasks(startCountTask);
        newGameCreatingStateService.save(state);
        requests.requestMaxPerformedQuestionCount(chatId, requestMsgId, state);
    }

    private void switchMsg4(long chatId, int requestMsgId, NewGameCreatingState state, String prop) {
        utils.switchMsg(4, chatId, requestMsgId, state, prop, null);
    }

    private void addMaxPerformedAndRequestNext(long chatId, NewGameCreatingState state,
                                               Integer maxPerformedQuestionsCount, int requestMsgId) {
        state.setMaxPerformedQuestionsCount(maxPerformedQuestionsCount);
        newGameCreatingStateService.save(state);
        requests.requestMinQuestionsCountInGame(chatId, requestMsgId, state);
    }

    private void switchMsg5(long chatId, int requestMsgId, NewGameCreatingState state, String prop) {
        utils.switchMsg(5, chatId, requestMsgId, state, prop, null);
    }

    private void addMinQuestionAndRequestNext(NewGameCreatingState state, long chatId,
                                              Integer minQuestionsInGame, int requestMsgId) {
        state.setMinQuestionsCountInGame(minQuestionsInGame);
        newGameCreatingStateService.save(state);
        requests.requestQuestionsCountToAdd(chatId, requestMsgId, state);
    }

    private void switchMsg6(long chatId, int requestMsgId, NewGameCreatingState state, String prop) {
        utils.switchMsg(6, chatId, requestMsgId, state, prop, null);
    }

    private void addQuestionsToAddAndPerformNext(NewGameCreatingState state, long chatId,
                                                 Integer questionsToAdd, int requestMsgId) {
        state.setQuestionsCountToAdd(questionsToAdd);
        newGameCreatingStateService.save(state);
        requests.requestMaxTimeMinutes(chatId, requestMsgId, state);
    }

    private void switchMsg7(long chatId, int requestMsgId, NewGameCreatingState state, String prop) {
        utils.switchMsg(7, chatId, requestMsgId, state, prop, null);
    }

    private void addTimeAndSaveGame(NewGameCreatingState state, Integer minutes, long chatId, int requestMsgId) {
        state.setMaxTimeMinutes(minutes);
        utils.saveNewGame(state);
        unblockAndUnrestrictChat(chatId);
        switchMsg8(chatId, requestMsgId, state, env.getProperty("messages.game.gameAdded"));
        newGameCreatingStateService.delete(state);
    }

    private void switchMsg8(long chatId, int requestMsgId, NewGameCreatingState state, String prop) {
        utils.switchMsg(8, chatId, requestMsgId, state, prop, null);
    }

    private void switchMsg9(long chatId, int requestMsgId, NewGameCreatingState state, String prop) {
        utils.switchMsg(9, chatId, requestMsgId, state, prop, null);
    }

    private void unblockAndUnrestrictChat(long chatId) {
        blockingManager.unblockAdminChat(chatId, env.getProperty("messages.admins.endGameCreating"));
        restrictingManager.unRestrictMembers(chatId);
    }
}

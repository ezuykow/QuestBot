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

    public void requestNewGameName(long chatId) {
        msgSender.send(chatId, env.getProperty("messages.game.requestNewGameName"));
    }

    public void requestQuestionGroups(String gameName, long chatId, int requestMsgId) {
        msgSender.edit(chatId, requestMsgId,
                String.format(
                        env.getProperty("messages.game.requestQuestionsGroups", "Error"), gameName),
                QuestionsGroupsKeyboard.createKeyboard(questionGroupService.findAll())
        );
    }

    public void requestMaxQuestionsCount(long chatId, int msgIdToEdit) {
        NewGameCreatingState state = utils.getNewGameCreatingState(chatId);
        msgSender.edit(chatId, msgIdToEdit,
                utils.createTextFromStateFields(
                        env.getProperty("messages.game.requestMaxQuestionsCount"), 2, state));
    }

    public void requestStartCountTasks(long chatId, int msgIdToEdit,
                                        NewGameCreatingState state) {
        msgSender.edit(chatId, msgIdToEdit,
                utils.createTextFromStateFields(
                        env.getProperty("messages.game.requestStartCountTasks"), 3, state));
    }

    public void requestMaxPerformedQuestionCount(long chatId, int msgIdToEdit, NewGameCreatingState state) {
        msgSender.edit(chatId, msgIdToEdit,
                utils.createTextFromStateFields(
                        env.getProperty("messages.game.requestMaxPerformedQuestionCount"), 4, state));
    }

    public void requestMinQuestionsCountInGame(long chatId, int msgIdToEdit, NewGameCreatingState state) {
        msgSender.edit(chatId, msgIdToEdit,
                utils.createTextFromStateFields(
                        env.getProperty("messages.game.requestMinQuestionsCountInGame"), 5, state));
    }

    public void requestQuestionsCountToAdd(long chatId, int msgIdToEdit, NewGameCreatingState state) {
        msgSender.edit(chatId, msgIdToEdit,
                utils.createTextFromStateFields(
                        env.getProperty("messages.game.requestQuestionsCountToAdd"), 6, state));
    }

    public void requestMaxTimeMinutes(long chatId, int msgIdToEdit, NewGameCreatingState state) {
        msgSender.edit(chatId, msgIdToEdit,
                utils.createTextFromStateFields(
                        env.getProperty("messages.game.requestMaxTimeMinutes"), 7, state));
    }
}

package ru.coffeecoders.questbot.actions;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.NewGameCreatingState;
import ru.coffeecoders.questbot.keyboards.QuestionsGroupsKeyboard;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.services.NewGameCreatingStateService;
import ru.coffeecoders.questbot.services.QuestionGroupService;

/**
 * @author ezuykow
 */
@Component
public class NewGameActions {

    private final NewGameCreatingStateService newGameCreatingStateService;
    private final QuestionGroupService questionGroupService;
    private final MessageSender msgSender;
    private final Environment env;

    public NewGameActions(NewGameCreatingStateService newGameCreatingStateService,
                          QuestionGroupService questionGroupService, MessageSender msgSender, Environment env) {
        this.newGameCreatingStateService = newGameCreatingStateService;
        this.questionGroupService = questionGroupService;
        this.msgSender = msgSender;
        this.env = env;
    }

    public void createNewGameCreatingState(long chatId) {
        newGameCreatingStateService.save(
                new NewGameCreatingState(chatId)
        );
        requestNewGameName(chatId);
    }

    public void addGameNameToStateAndRequestNextPart(long chatId, NewGameCreatingState state,
                                                     String gameName, int answerMsgId) {
        state.setGameName(gameName);
        newGameCreatingStateService.save(state);
        int requestMsgId = getRequestMsgIdAndDeleteAnswerMsg(chatId, answerMsgId);
        requestQuestionGroups(gameName, chatId, requestMsgId);
    }

    public NewGameCreatingState getNewGameCreatingState(long chatId) {
        return newGameCreatingStateService.findById(chatId)
                .orElseThrow(() ->
                {
                    msgSender.send(chatId, env.getProperty("messages.somethingWrong"));
                    return new RuntimeException("Этого, конечно, никогда не будет, нооо... пиздец, короче");
                });
    }

    private void requestNewGameName(long chatId) {
        msgSender.send(chatId, env.getProperty("messages.game.requestNewGameName"));
    }

    private void requestQuestionGroups(String gameName, long chatId, int requestMsgId) {
        msgSender.edit(chatId, requestMsgId,
                String.format(
                        env.getProperty("messages.game.requestQuestionsGroups", "Error"), gameName),
                QuestionsGroupsKeyboard.createKeyboard(questionGroupService.findAll())
        );
    }

    /*private void requestStartCountTasks(String gameName, long chatId, int answerMsgId) {
        msgSender.edit(chatId, requestMsgId,
                String.format(
                        env.getProperty("messages.game.requestStartCountTasks", "Error"), gameName),
                null);
    }*/

    private int getRequestMsgIdAndDeleteAnswerMsg(long chatId, int answerMsgId) {
        msgSender.sendDelete(chatId, answerMsgId);
        return answerMsgId - 1;
    }
}

package ru.coffeecoders.questbot.actions;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.NewGameCreatingState;
import ru.coffeecoders.questbot.entities.QuestionGroup;
import ru.coffeecoders.questbot.keyboards.QuestionsGroupsKeyboard;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.services.NewGameCreatingStateService;
import ru.coffeecoders.questbot.services.QuestionGroupService;
import ru.coffeecoders.questbot.validators.QuestionsValidator;

import java.util.List;

/**
 * @author ezuykow
 */
@Component
public class NewGameActions {

    private final NewGameCreatingStateService newGameCreatingStateService;
    private final QuestionGroupService questionGroupService;
    private final QuestionsValidator questionsValidator;
    private final MessageSender msgSender;
    private final Environment env;

    public NewGameActions(NewGameCreatingStateService newGameCreatingStateService,
                          QuestionGroupService questionGroupService, QuestionsValidator questionsValidator, MessageSender msgSender, Environment env) {
        this.newGameCreatingStateService = newGameCreatingStateService;
        this.questionGroupService = questionGroupService;
        this.questionsValidator = questionsValidator;
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

    public void addSelectedQuestionGroupAndRefreshMsg(long chatId, int msgId, int questionGroupId) {
        int[] allStateGroupsIds = addQuestionGroupIdToState(chatId, questionGroupId);
        String stateGroupsNames = getGroupsNames(allStateGroupsIds);
        msgSender.edit(chatId, msgId,
                String.format(env.getProperty("messages.game.addedQuestionGroup", "Error"),
                        stateGroupsNames),
                QuestionsGroupsKeyboard.createKeyboard(questionGroupService.findAll()
                        .stream()
                        .filter(g -> !ArrayUtils.contains(allStateGroupsIds, g.getGroupId()))
                        .toList()
                )
        );
    }

    public void stopSelectingQuestionsGroupsAndRequestNextPart(long chatId, int msgId) {
        requestMaxQuestionsCount(chatId, msgId);
    }

    public void addMaxQuestionsCountToStateAndRequestNextPart(long chatId, NewGameCreatingState state,
                                                              String text, int msgId) {
        Integer maxQuestionCount = parseTextToInteger(text);
        int requestMsgId = getRequestMsgIdAndDeleteAnswerMsg(chatId, msgId);
        if (maxQuestionCount != null) {
            if (questionsValidator.isRequestedQuestionCountNotMoreThanWeHaveByGroups(
                    maxQuestionCount, state.getGroupsIds())) {

                state.setMaxQuestionsCount(maxQuestionCount);
                newGameCreatingStateService.save(state);
                requestStartCountTasks(chatId, requestMsgId, maxQuestionCount);
            } else {
                msgSender.edit(chatId, requestMsgId,
                        env.getProperty("messages.game.invalidQuestionCount")
                                + env.getProperty("messages.game.requestMaxQuestionsCountSimple"),
                        null
                );
            }
        } else {
            msgSender.edit(chatId, requestMsgId,
                    env.getProperty("messages.game.invalidNumber")
                            + env.getProperty("messages.game.requestMaxQuestionsCountSimple"),
                    null
            );
        }
    }

    public void addStartCountTaskToStateAndRequestNextPart(long chatId, NewGameCreatingState state,
                                                           String text, int msgId) {
        Integer startCountTask = parseTextToInteger(text);
        int requestMsgId = getRequestMsgIdAndDeleteAnswerMsg(chatId, msgId);
        int maxQuestionCount = state.getMaxQuestionsCount();
        if (startCountTask != null) {
            if (startCountTask <= maxQuestionCount) {
                state.setStartCountTasks(startCountTask);
                newGameCreatingStateService.save(state);
                requestMaxPerformedQuestionCount(chatId, requestMsgId, startCountTask);
            } else {
                msgSender.edit(chatId, requestMsgId,
                        env.getProperty("messages.game.startQMoreMaxQ")
                                + String.format(
                                env.getProperty("messages.game.requestStartCountTasks", "Error"),
                                maxQuestionCount),
                        null
                );
            }
        } else {
            msgSender.edit(chatId, requestMsgId,
                    env.getProperty("messages.game.invalidNumber")
                            + String.format(
                                env.getProperty("messages.game.requestStartCountTasks", "Error"),
                                maxQuestionCount),
                    null
            );
        }
    }

    public void addMaxPerformedQuestionsCountToStateAndRequestNextPart(long chatId, NewGameCreatingState state,
                                                                       String text, int msgId) {
        Integer maxPerformedQuestionsCount = parseTextToInteger(text);
        int requestMsgId = getRequestMsgIdAndDeleteAnswerMsg(chatId, msgId);
        int startQuestions = state.getStartCountTasks();
        if (maxPerformedQuestionsCount != null) {
            if (maxPerformedQuestionsCount <= state.getMaxQuestionsCount()) {
                state.setMaxPerformedQuestionsCount(maxPerformedQuestionsCount);
                newGameCreatingStateService.save(state);
                requestMinQuestionsCountInGame(chatId, msgId, maxPerformedQuestionsCount);
            } else {
                msgSender.edit(chatId, requestMsgId,
                        env.getProperty("messages.game.maxPerformedQMoreMaxQ")
                                + String.format(
                                env.getProperty("messages.game.requestMaxPerformedQuestionCount", "Error"),
                                startQuestions),
                        null
                );
            }
        } else {
            msgSender.edit(chatId, requestMsgId,
                    env.getProperty("messages.game.invalidNumber")
                            + String.format(
                            env.getProperty("messages.game.requestMaxPerformedQuestionCount", "Error"),
                            startQuestions),
                    null
            );
        }
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

    private void requestMaxQuestionsCount(long chatId, int msgIdToEdit) {
        msgSender.edit(chatId, msgIdToEdit,
                String.format(
                        env.getProperty("messages.game.requestMaxQuestionsCount", "Error"),
                        getGroupsNames(getNewGameCreatingState(chatId).getGroupsIds())),
                null);
    }

    private void requestStartCountTasks(long chatId, int msgIdToEdit, int maxQuestionCount) {
        msgSender.edit(chatId, msgIdToEdit,
                String.format(
                        env.getProperty("messages.game.requestStartCountTasks", "Error"),
                        maxQuestionCount),
                null);
    }

    private void requestMaxPerformedQuestionCount(long chatId, int msgIdToEdit, int startCountTask) {
        msgSender.edit(chatId, msgIdToEdit,
                String.format(
                        env.getProperty("messages.game.requestMaxPerformedQuestionCount", "Error"),
                        startCountTask),
                null);
    }

    private void requestMinQuestionsCountInGame(long chatId, int msgIdToEdit, Integer maxPerformedQuestionsCount) {
        msgSender.edit(chatId, msgIdToEdit,
                String.format(
                        env.getProperty("messages.game.requestMinQuestionsCountInGame", "Error"),
                        maxPerformedQuestionsCount),
                null);
    }

    private int getRequestMsgIdAndDeleteAnswerMsg(long chatId, int answerMsgId) {
        msgSender.sendDelete(chatId, answerMsgId);
        return answerMsgId - 1;
    }

    private String getGroupsNames(int[] allStateGroupsIds) {
        StringBuilder sb = new StringBuilder();
        List<QuestionGroup> groups = questionGroupService.findAll();
        for (int i = 0; i < allStateGroupsIds.length; i++) {
            final int id = allStateGroupsIds[i];
            groups.stream().filter(g -> g.getGroupId() == id).findAny()
                    .ifPresent(g -> sb.append(g.getGroupName()));
            if (i < allStateGroupsIds.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    private int[] addQuestionGroupIdToState(long chatId, int questionGroupId) {
        NewGameCreatingState state = getNewGameCreatingState(chatId);
        int[] groupsIds = ArrayUtils.add(state.getGroupsIds(), questionGroupId);
        state.setGroupsIds(groupsIds);
        newGameCreatingStateService.save(state);
        return groupsIds;
    }

    private Integer parseTextToInteger(String text) {
        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

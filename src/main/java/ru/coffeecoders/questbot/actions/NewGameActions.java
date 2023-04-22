package ru.coffeecoders.questbot.actions;

import com.pengrad.telegrambot.model.ChatPermissions;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.AdminChat;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.entities.NewGameCreatingState;
import ru.coffeecoders.questbot.entities.QuestionGroup;
import ru.coffeecoders.questbot.keyboards.QuestionsGroupsKeyboard;
import ru.coffeecoders.questbot.senders.MessageSender;
import ru.coffeecoders.questbot.services.*;
import ru.coffeecoders.questbot.validators.QuestionsValidator;

import java.util.Arrays;
import java.util.List;

/**
 * @author ezuykow
 */
@Component
public class NewGameActions {

    private final NewGameCreatingStateService newGameCreatingStateService;
    private final QuestionGroupService questionGroupService;
    private final QuestionsValidator questionsValidator;
    private final GameService gameService;
    private final AdminChatService adminChatService;
    private final AdminChatMembersService adminChatMembersService;
    private final MessageSender msgSender;
    private final Environment env;

    public NewGameActions(NewGameCreatingStateService newGameCreatingStateService,
                          QuestionGroupService questionGroupService, QuestionsValidator questionsValidator, GameService gameService, AdminChatService adminChatService, AdminChatMembersService adminChatMembersService, MessageSender msgSender, Environment env) {
        this.newGameCreatingStateService = newGameCreatingStateService;
        this.questionGroupService = questionGroupService;
        this.questionsValidator = questionsValidator;
        this.gameService = gameService;
        this.adminChatService = adminChatService;
        this.adminChatMembersService = adminChatMembersService;
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
        msgSender.sendDelete(chatId, answerMsgId);
        state.setGameName(gameName);
        state.setRequestMsgId(answerMsgId - 1);
        newGameCreatingStateService.save(state);
        requestQuestionGroups(gameName, chatId, answerMsgId - 1);
    }

    public void addSelectedQuestionGroupAndRefreshMsg(long chatId, int msgId, int questionGroupId) {
        int[] allStateGroupsIds = addQuestionGroupIdToState(chatId, questionGroupId);
        String stateGroupsNames = getGroupsNames(allStateGroupsIds);
        msgSender.edit(chatId, msgId,
                String.format(env.getProperty("messages.game.addedQuestionGroup", "Error"),
                        getNewGameCreatingState(chatId).getGameName(),
                        stateGroupsNames),
                QuestionsGroupsKeyboard.createKeyboard(questionGroupService.findAll()
                        .stream()
                        .filter(g -> !ArrayUtils.contains(allStateGroupsIds, g.getGroupId()))
                        .toList()
                )
        );
    }

    public void stopSelectingQuestionsGroupsAndRequestNextPart(long chatId, int msgId) {
        if (getNewGameCreatingState(chatId).getGroupsIds() != null) {
            requestMaxQuestionsCount(chatId, msgId);
        }
    }

    public void addMaxQuestionsCountToStateAndRequestNextPart(long chatId, NewGameCreatingState state,
                                                              String text, int msgId) {
        Integer maxQuestionCount = parseTextToInteger(text);
        msgSender.sendDelete(chatId, msgId);
        int requestMsgId = state.getRequestMsgId();
        if (maxQuestionCount != null && maxQuestionCount > 0) {
            if (questionsValidator.isRequestedQuestionCountNotMoreThanWeHaveByGroups(
                    maxQuestionCount, state.getGroupsIds())) {

                state.setMaxQuestionsCount(maxQuestionCount);
                newGameCreatingStateService.save(state);
                requestStartCountTasks(chatId, requestMsgId, maxQuestionCount, state);
            } else {
                msgSender.edit(chatId, requestMsgId,
                        env.getProperty("messages.game.invalidQuestionCount")
                                + String.format(
                                env.getProperty("messages.game.requestMaxQuestionsCount", "Error"),
                                state.getGameName(),
                                getGroupsNames(state.getGroupsIds()))
                );
            }
        } else {
            msgSender.edit(chatId, requestMsgId,
                    env.getProperty("messages.game.invalidNumber")
                            + String.format(
                            env.getProperty("messages.game.requestMaxQuestionsCount", "Error"),
                            state.getGameName(),
                            getGroupsNames(state.getGroupsIds()))
            );
        }
    }

    public void addStartCountTaskToStateAndRequestNextPart(long chatId, NewGameCreatingState state,
                                                           String text, int msgId) {
        Integer startCountTask = parseTextToInteger(text);
        msgSender.sendDelete(chatId, msgId);
        int requestMsgId = state.getRequestMsgId();
        int maxQuestionCount = state.getMaxQuestionsCount();
        if (startCountTask != null && startCountTask > 0) {
            if (startCountTask <= maxQuestionCount) {
                state.setStartCountTasks(startCountTask);
                newGameCreatingStateService.save(state);
                requestMaxPerformedQuestionCount(chatId, requestMsgId, startCountTask, state);
            } else {
                msgSender.edit(chatId, requestMsgId,
                        env.getProperty("messages.game.startQMoreMaxQ")
                                + String.format(
                                env.getProperty("messages.game.requestStartCountTasks", "Error"),
                                state.getGameName(),
                                getGroupsNames(state.getGroupsIds()),
                                state.getMaxQuestionsCount())
                );

            }
        } else {
            msgSender.edit(chatId, requestMsgId,
                    env.getProperty("messages.game.invalidNumber")
                            + String.format(
                            env.getProperty("messages.game.requestStartCountTasks", "Error"),
                            state.getGameName(),
                            getGroupsNames(state.getGroupsIds()),
                            state.getMaxQuestionsCount())
            );
        }
    }

    public void addMaxPerformedQuestionsCountToStateAndRequestNextPart(long chatId, NewGameCreatingState state,
                                                                       String text, int msgId) {
        Integer maxPerformedQuestionsCount = parseTextToInteger(text);
        msgSender.sendDelete(chatId, msgId);
        int requestMsgId = state.getRequestMsgId();
        if (maxPerformedQuestionsCount != null && maxPerformedQuestionsCount > 0) {
            if (maxPerformedQuestionsCount <= state.getMaxQuestionsCount()) {
                state.setMaxPerformedQuestionsCount(maxPerformedQuestionsCount);
                newGameCreatingStateService.save(state);
                requestMinQuestionsCountInGame(chatId, requestMsgId, maxPerformedQuestionsCount, state);
            } else {
                msgSender.edit(chatId, requestMsgId,
                        env.getProperty("messages.game.maxPerformedQMoreMaxQ")
                                + String.format(
                                env.getProperty("messages.game.requestMaxPerformedQuestionCount", "Error"),
                                state.getGameName(),
                                getGroupsNames(state.getGroupsIds()),
                                state.getMaxQuestionsCount(),
                                state.getStartCountTasks())
                );
            }
        } else {
            msgSender.edit(chatId, requestMsgId,
                    env.getProperty("messages.game.invalidNumber")
                            + String.format(
                            env.getProperty("messages.game.requestMaxPerformedQuestionCount", "Error"),
                            state.getGameName(),
                            getGroupsNames(state.getGroupsIds()),
                            state.getMaxQuestionsCount(),
                            state.getStartCountTasks())
            );
        }
    }

    public void addMinQuestionsCountInGameAndRequestNextPart(long chatId, NewGameCreatingState state,
                                                             String text, int msgId) {
        Integer minQuestionsInGame = parseTextToInteger(text);
        msgSender.sendDelete(chatId, msgId);
        int requestMsgId = state.getRequestMsgId();
        if (minQuestionsInGame != null && minQuestionsInGame >= 0) {
            state.setMinQuestionsCountInGame(minQuestionsInGame);
            newGameCreatingStateService.save(state);
            requestQuestionsCountToAdd(chatId, requestMsgId, minQuestionsInGame, state);
        } else {
            msgSender.edit(chatId, requestMsgId,
                    env.getProperty("messages.game.invalidNumber")
                    + String.format(
                            env.getProperty("messages.game.requestMinQuestionsCountInGame", "Error"),
                            state.getGameName(),
                            getGroupsNames(state.getGroupsIds()),
                            state.getMaxQuestionsCount(),
                            state.getStartCountTasks(),
                            state.getMaxPerformedQuestionsCount())
            );
        }
    }

    public void addQuestionsCountToAddAndRequestNextPart(long chatId, NewGameCreatingState state,
                                                         String text, int msgId) {
        Integer questionsToAdd = parseTextToInteger(text);
        msgSender.sendDelete(chatId, msgId);
        int requestMsgId = state.getRequestMsgId();
        if (questionsToAdd != null && questionsToAdd >= 0) {
            state.setQuestionsCountToAdd(questionsToAdd);
            newGameCreatingStateService.save(state);
            requestMaxTimeMinutes(chatId, requestMsgId, questionsToAdd, state);
        } else {
            msgSender.edit(chatId, requestMsgId,
                    env.getProperty("messages.game.invalidNumber")
                            + String.format(
                            env.getProperty("messages.game.requestQuestionsCountToAdd", "Error"),
                            state.getGameName(),
                            getGroupsNames(state.getGroupsIds()),
                            state.getMaxQuestionsCount(),
                            state.getStartCountTasks(),
                            state.getMaxPerformedQuestionsCount(),
                            state.getMinQuestionsCountInGame())
            );
        }
    }

    public void addMaxTimeMinutesToStateAmdSaveNewGame(long chatId, NewGameCreatingState state,
                                                       String text, int msgId) {
        Integer minutes = parseTextToInteger(text);
        msgSender.sendDelete(chatId, msgId);
        int requestMsgId = state.getRequestMsgId();
        int toAdd = state.getQuestionsCountToAdd();
        if (minutes != null && minutes > 0) {
            state.setMaxTimeMinutes(minutes);
            saveNewGame(state);
            removeBlockedByAdminOnAdminChat(chatId);
            unRestrictAllMembers(chatId);
            msgSender.edit(chatId, requestMsgId,
                    String.format(env.getProperty("messages.game.gameAdded", "Error"),
                            state.getGameName(),
                            getGroupsNames(state.getGroupsIds()),
                            state.getMaxQuestionsCount(),
                            state.getStartCountTasks(),
                            state.getMaxPerformedQuestionsCount(),
                            state.getMinQuestionsCountInGame(),
                            state.getQuestionsCountToAdd(),
                            state.getMaxTimeMinutes())
            );
            newGameCreatingStateService.delete(state);
        } else {
            msgSender.edit(chatId, requestMsgId,
                    env.getProperty("messages.game.invalidNumber")
                            + String.format(
                            env.getProperty("messages.game.requestMaxTimeMinutes", "Error"),
                            state.getGameName(),
                            getGroupsNames(state.getGroupsIds()),
                            state.getMaxQuestionsCount(),
                            state.getStartCountTasks(),
                            state.getMaxPerformedQuestionsCount(),
                            state.getMinQuestionsCountInGame(),
                            state.getQuestionsCountToAdd())
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
                        getNewGameCreatingState(chatId).getGameName(),
                        getGroupsNames(getNewGameCreatingState(chatId).getGroupsIds())));
    }

    private void requestStartCountTasks(long chatId, int msgIdToEdit, int maxQuestionCount,
                                        NewGameCreatingState state) {
        msgSender.edit(chatId, msgIdToEdit,
                String.format(
                        env.getProperty("messages.game.requestStartCountTasks", "Error"),
                        state.getGameName(),
                        getGroupsNames(state.getGroupsIds()),
                        state.getMaxQuestionsCount()
                )
        );
    }

    private void requestMaxPerformedQuestionCount(long chatId, int msgIdToEdit, int startCountTask, NewGameCreatingState state) {
        msgSender.edit(chatId, msgIdToEdit,
                String.format(
                        env.getProperty("messages.game.requestMaxPerformedQuestionCount", "Error"),
                        state.getGameName(),
                        getGroupsNames(state.getGroupsIds()),
                        state.getMaxQuestionsCount(),
                        state.getStartCountTasks())
        );
    }

    private void requestMinQuestionsCountInGame(long chatId, int msgIdToEdit, Integer maxPerformedQuestionsCount, NewGameCreatingState state) {
        msgSender.edit(chatId, msgIdToEdit,
                String.format(
                        env.getProperty("messages.game.requestMinQuestionsCountInGame", "Error"),
                        state.getGameName(),
                        getGroupsNames(state.getGroupsIds()),
                        state.getMaxQuestionsCount(),
                        state.getStartCountTasks(),
                        state.getMaxPerformedQuestionsCount())
        );
    }

    private void requestQuestionsCountToAdd(long chatId, int msgIdToEdit, Integer minQuestionsInGame, NewGameCreatingState state) {
        msgSender.edit(chatId, msgIdToEdit,
                String.format(
                        env.getProperty("messages.game.requestQuestionsCountToAdd", "Error"),
                        state.getGameName(),
                        getGroupsNames(state.getGroupsIds()),
                        state.getMaxQuestionsCount(),
                        state.getStartCountTasks(),
                        state.getMaxPerformedQuestionsCount(),
                        state.getMinQuestionsCountInGame())
        );
    }

    private void requestMaxTimeMinutes(long chatId, int msgIdToEdit, Integer questionsToAdd, NewGameCreatingState state) {
        msgSender.edit(chatId, msgIdToEdit,
                String.format(
                        env.getProperty("messages.game.requestMaxTimeMinutes", "Error"),
                        state.getGameName(),
                        getGroupsNames(state.getGroupsIds()),
                        state.getMaxQuestionsCount(),
                        state.getStartCountTasks(),
                        state.getMaxPerformedQuestionsCount(),
                        state.getMinQuestionsCountInGame(),
                        state.getQuestionsCountToAdd())
        );
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

    private void saveNewGame(NewGameCreatingState state) {
        gameService.save(
                new Game(
                        state.getGameName(),
                        state.getGroupsIds(),
                        state.getMaxTimeMinutes(),
                        state.getMaxQuestionsCount(),
                        state.getMaxPerformedQuestionsCount(),
                        state.getMinQuestionsCountInGame(),
                        state.getQuestionsCountToAdd(),
                        state.getStartCountTasks()
                )
        );
    }

    private void removeBlockedByAdminOnAdminChat(long chatId) {
        AdminChat currentAdminChat = adminChatService.findById(chatId).get();
        currentAdminChat.setBlockedByAdminId(0);
        adminChatService.save(currentAdminChat);
    }

    private void unRestrictAllMembers(long chatId) {
        ChatPermissions permissions = new ChatPermissions()
                .canSendMessages(true)
                .canSendOtherMessages(true);

        Arrays.stream(adminChatMembersService.findByChatId(chatId).get().getMembers())
                .forEach(id -> msgSender.sendRestrictChatMember(chatId, id, permissions));
    }
}

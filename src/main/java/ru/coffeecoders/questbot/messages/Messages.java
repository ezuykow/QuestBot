package ru.coffeecoders.questbot.messages;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.properties.PropertySeed;

import java.util.Map;

/**
 * @author ezuykow
 */
@Component
public class Messages {

    private final Map<String, PropertySeed> properties;

    public Messages(Map<String, PropertySeed> properties) {
        this.properties = properties;
    }

    //-----------------API START-----------------

    //general messages

    public String welcome() {
        return properties.get("messages.welcome").getActualProperty();
    }

    public String startUp() {
        return properties.get("messages.startUp").getActualProperty();
    }

    public String stopBot() {
        return properties.get("messages.stopBot").getActualProperty();
    }

    public String answersWrong() {
        return properties.get("answers.wrong").getActualProperty();
    }

    public String endGamePostfix() {
        return properties.get("endGame.postfix").getActualProperty();
    }

    public String escapes() {
        return MessageBuilder.ESCAPES;
    }

    public String selectGroupName() {
        return properties.get("messages.QuestionsViewer.selectGroupName").getActualProperty();
    }

    public String gameInfoHat() {
        return properties.get("messages.gameInfo.hat").getActualProperty();
    }

    public String gameInfoRemainingTime() {
        return properties.get("messages.gameInfo.time").getActualProperty();
    }

    public String gameInfoRemainingTimePostfix() {
        return properties.get("messages.gameInfo.timePostfix").getActualProperty();
    }

    public String gameInfoScorePrefix() {
        return properties.get("messages.gameInfo.scorePrefix").getActualProperty();
    }

    public String gameInfoScorePostfix() {
        return properties.get("messages.gameInfo.scorePostfix").getActualProperty();
    }

    public String answerAcceptedPrefix() {
        return properties.get("messages.answersAccepting.prefix").getActualProperty();
    }

    public String answerAcceptedMiddle() {
        return properties.get("messages.answersAccepting.middle").getActualProperty();
    }

    public String answerAcceptedPostfix() {
        return properties.get("messages.answersAccepting.postfix").getActualProperty();
    }

    public String answerAcceptedAdd() {
        return properties.get("messages.answersAccepting.add").getActualProperty();
    }

    public String tasksViewerQuestion() {
        return properties.get("messages.tasksViewer.question").getActualProperty();
    }

    public String tasksViewerAnswerFormat() {
        return properties.get("messages.tasksViewer.format").getActualProperty();
    }

    public String tasksViewerAdditional() {
        return properties.get("messages.tasksViewer.add").getActualProperty();
    }

    //endgame

    public String endGameTime() {
        return properties.get("messages.endGame.time").getActualProperty();
    }

    public String endGameTeamPrefix() {
        return properties.get("messages.endGame.teamPrefix").getActualProperty();
    }

    public String endGameTeamMiddle() {
        return properties.get("messages.endGame.teamMiddle").getActualProperty();
    }

    public String endGameTeamPostfix() {
        return properties.get("messages.endGame.teamPostfix").getActualProperty();
    }

    public String endGameQuestionsEnded() {
        return properties.get("messages.endGame.questionsEnded").getActualProperty();
    }

    public String endGameInterrupt() {
        return properties.get("messages.endGame.interrupt").getActualProperty();
    }

    //owner messages

    public String chatIsAdminNow() {
        return properties.get("messages.owner.chatIsAdminNow").getActualProperty();
    }

    public String chatIsGlobalNow() {
        return properties.get("messages.owner.chatIsGlobalNow").getActualProperty();
    }

    public String emptyPromotionList() {
        return properties.get("messages.owner.emptyPromotionList").getActualProperty();
    }

    public String promote() {
        return properties.get("messages.owner.promote").getActualProperty();
    }

    public String demote() {
        return properties.get("messages.owner.demote").getActualProperty();
    }

    public String userPromoted() {
        return properties.get("messages.owner.userPromoted").getActualProperty();
    }

    public String userDemoted() {
        return properties.get("messages.owner.userDemoted").getActualProperty();
    }

    public String startCmdFailed() {
        return properties.get("messages.owner.validation.startCmdFailed").getActualProperty();
    }

    public String adminOnCmdFailed() {
        return properties.get("messages.owner.validation.adminOnCmdFailed").getActualProperty();
    }

    public String chatIsNotAdmin() {
        return properties.get("messages.owner.validation.chatIsNotAdmin").getActualProperty();
    }

    //admins messages

    public String invalidMsg() {
        return properties.get("messages.admins.invalidMsg").getActualProperty();
    }

    public String cmdSendByNotAdmin() {
        return properties.get("messages.admins.cmdSendByNotAdmin").getActualProperty();
    }

    public String adminCmdInGlobalChat() {
        return properties.get("messages.admins.adminCmdInGlobalChat").getActualProperty();
    }

    public String isOwnerCommand() {
        return properties.get("messages.admins.isOwnerCommand").getActualProperty();
    }

    public String startGameCreating() {
        return properties.get("messages.admins.startGameCreating").getActualProperty();
    }

    public String endGameCreating() {
        return properties.get("messages.admins.endGameCreating").getActualProperty();
    }

    public String startQuestionView() {
        return properties.get("messages.admins.startQuestionView").getActualProperty();
    }

    public String endQuestionView() {
        return properties.get("messages.admins.endQuestionView").getActualProperty();
    }

    public String startGamesView() {
        return properties.get("messages.admins.startGamesView").getActualProperty();
    }

    public String endGamesView() {
        return properties.get("messages.admins.endGamesView").getActualProperty();
    }

    public String cmdForGlobalChat() {
        return properties.get("messages.admins.cmdForGlobalChat").getActualProperty();
    }

    public String chatNotInGame() {
        return properties.get("messages.admins.chatNotInGame").getActualProperty();
    }

    public String choosePreparingGame() {
        return properties.get("messages.admins.choosePreparingGame").getActualProperty();
    }

    //players messages

    public String haventStartedGame() {
        return properties.get("messages.players.haventStartedGame").getActualProperty();
    }

    public String enterTeamCount() {
        return properties.get("messages.players.enterTeamCount").getActualProperty();
    }

    public String teamChooserHat() {
        return properties.get("messages.players.teamChooserHat").getDefaultProperty();
    }

    //members messages

    public String welcomeAdminPrefix() {
        return properties.get("messages.members.welcomeAdminPrefix").getActualProperty();
    }

    public String welcomeAdminSuffix() {
        return properties.get("messages.members.welcomeAdminSuffix").getActualProperty();
    }

    public String byeAdmin() {
        return properties.get("messages.members.byeAdmin").getActualProperty();
    }

    //documents messages

    public String emptyQuestionList() {
        return properties.get("messages.documents.emptyQuestionList").getActualProperty();
    }

    public String fromNotAdmin() {
        return properties.get("messages.documents.fromNotAdmin").getActualProperty();
    }

    public String wrongDocumentType() {
        return properties.get("messages.documents.wrongDocumentType").getActualProperty();
    }

    public String emptyQuestionsNotAdded() {
        return properties.get("messages.documents.emptyQuestionsNotAdded").getActualProperty();
    }

    public String equalsQuestionsUpdated() {
        return properties.get("messages.documents.equalsQuestionsUpdated").getActualProperty();
    }

    public String questionsAdded() {
        return properties.get("messages.documents.questionsAdded").getActualProperty();
    }

    public String noOneQuestionAdded() {
        return properties.get("messages.documents.noOneQuestionAdded").getActualProperty();
    }

    public String defaultQuestionGroup() {
        return properties.get("messages.documents.defaultQuestionGroup").getActualProperty();
    }

    //questions messages

    public String emptyList() {
        return properties.get("messages.questions.emptyList").getActualProperty();
    }

    //games messages

    public String requestNewGameName() {
        return properties.get("messages.games.requestNewGameName").getActualProperty();
    }

    public String requestQuestionsGroups() {
        return properties.get("messages.games.requestQuestionsGroups").getActualProperty();
    }

    public String addedQuestionGroup() {
        return properties.get("messages.games.addedQuestionGroup").getActualProperty();
    }

    public String requestMaxQuestionsCount() {
        return properties.get("messages.games.requestMaxQuestionsCount").getActualProperty();
    }

    public String requestStartCountTasks() {
        return properties.get("messages.games.requestStartCountTasks").getActualProperty();
    }

    public String requestMaxPerformedQuestionCount() {
        return properties.get("messages.games.requestMaxPerformedQuestionCount").getActualProperty();
    }

    public String requestMinQuestionsCountInGame() {
        return properties.get("messages.games.requestMinQuestionsCountInGame").getActualProperty();
    }

    public String requestQuestionsCountToAdd() {
        return properties.get("messages.games.requestQuestionsCountToAdd").getActualProperty();
    }

    public String requestMaxTimeMinutes() {
        return properties.get("messages.games.requestMaxTimeMinutes").getActualProperty();
    }

    public String requestShuffle() {
        return properties.get("messages.games.requestShuffle").getActualProperty();
    }

    public String gameAdded() {
        return properties.get("messages.games.gameAdded").getActualProperty();
    }

    public String gameInfo() {
        return properties.get("messages.games.gameInfo").getActualProperty();
    }

    public String nameAlreadyTaken() {
        return properties.get("messages.games.nameAlreadyTaken").getActualProperty();
    }

    public String invalidNumber() {
        return properties.get("messages.games.invalidNumber").getActualProperty();
    }

    public String invalidQuestionCount() {
        return properties.get("messages.games.invalidQuestionCount").getActualProperty();
    }

    public String startQMoreMaxQ() {
        return properties.get("messages.games.startQMoreMaxQ").getActualProperty();
    }

    public String maxPerformedQMoreMaxQ() {
        return properties.get("messages.games.maxPerformedQMoreMaxQ").getActualProperty();
    }

    public String emptyGamesList() {
        return properties.get("messages.games.emptyGamesList").getActualProperty();
    }

    public String failedDeletingGame() {
        return properties.get("messages.games.failedDeletingGame").getActualProperty();
    }

    public String notEnoughQuestions() {
        return properties.get("messages.games.notEnoughQuestions").getActualProperty();
    }

    public String prepareGameStartedHint() {
        return properties.get("messages.games.prepareGameStartedHint").getActualProperty();
    }

    public String prepareInterrupted() {
        return properties.get("messages.games.prepareInterrupted").getActualProperty();
    }

    public String gameStarted() {
        return properties.get("messages.games.gameStarted").getActualProperty();
    }

    public String gameStartedHint() {
        return properties.get("messages.games.gameStartedHint").getActualProperty();
    }

    public String requestAdditionWithTask() {
        return properties.get("messages.games.requestAdditionWithTask").getActualProperty();
    }

    public String time() {
        return properties.get("messages.games.time").getActualProperty();
    }

    //-----------------API END-----------------

}

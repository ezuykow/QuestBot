package ru.coffeecoders.questbot.messages;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author ezuykow
 */
@Component
public class Messages {

    private final Environment env;

    public Messages(Environment env) {
        this.env = env;
    }

    //-----------------API START-----------------

    //general messages

    public String welcome() {
        return env.getProperty("messages.welcome");
    }

    public String startUp() {
        return env.getProperty("messages.startUp");
    }

    public String stopBot() {
        return env.getProperty("messages.stopBot");
    }

    //owner messages

    public String chatIsAdminNow() {
        return env.getProperty("messages.owner.chatIsAdminNow");
    }

    public String chatIsGlobalNow() {
        return env.getProperty("messages.owner.chatIsGlobalNow");
    }

    public String emptyPromotionList() {
        return env.getProperty("messages.owner.emptyPromotionList");
    }

    public String promote() {
        return env.getProperty("messages.owner.promote");
    }

    public String demote() {
        return env.getProperty("messages.owner.demote");
    }

    public String userPromoted() {
        return env.getProperty("messages.owner.userPromoted");
    }

    public String userDemoted() {
        return env.getProperty("messages.owner.userDemoted");
    }

    public String startCmdFailed() {
        return env.getProperty("messages.owner.validation.startCmdFailed");
    }

    public String adminOnCmdFailed() {
        return env.getProperty("messages.owner.validation.adminOnCmdFailed");
    }

    public String chatIsNotAdmin() {
        return env.getProperty("messages.owner.validation.chatIsNotAdmin");
    }

    //admins messages

    public String invalidMsg() {
        return env.getProperty("messages.admins.invalidMsg");
    }

    public String cmdSendByNotAdmin() {
        return env.getProperty("messages.admins.cmdSendByNotAdmin");
    }

    public String adminCmdInGlobalChat() {
        return env.getProperty("messages.admins.adminCmdInGlobalChat");
    }

    public String gameCmdInAdminChat() {
        return env.getProperty("messages.admins.gameCmdInAdminChat");
    }

    public String isOwnerCommand() {
        return env.getProperty("messages.admins.isOwnerCommand");
    }

    public String startGameCreating() {
        return env.getProperty("messages.admins.startGameCreating");
    }

    public String endGameCreating() {
        return env.getProperty("messages.admins.endGameCreating");
    }

    public String startQuestionView() {
        return env.getProperty("messages.admins.startQuestionView");
    }

    public String endQuestionView() {
        return env.getProperty("messages.admins.endQuestionView");
    }

    public String startGamesView() {
        return env.getProperty("messages.admins.startGamesView");
    }

    public String endGamesView() {
        return env.getProperty("messages.admins.endGamesView");
    }

    public String cmdForGlobalChat() {
        return env.getProperty("messages.admins.cmdForGlobalChat");
    }

    public String chatNotInGame() {
        return env.getProperty("messages.admins.chatNotInGame");
    }

    public String choosePreparingGame() {
        return env.getProperty("messages.admins.choosePreparingGame");
    }

    //players messages

    public String haventStartedGame() {
        return env.getProperty("messages.players.haventStartedGame");
    }

    public String enterTeamName() {
        return env.getProperty("messages.players.enterTeamName");
    }

    public String noTeamsRegisteredYet() {
        return env.getProperty("messages.players.noTeamsRegisteredYet");
    }

    public String chooseYourTeam() {
        return env.getProperty("messages.players.chooseYourTeam");
    }

    //members messages

    public String welcomePrefix() {
        return env.getProperty("messages.members.welcomePrefix");
    }

    public String welcomeSuffix() {
        return env.getProperty("messages.members.welcomeSuffix");
    }

    public String byePrefix() {
        return env.getProperty("messages.members.byePrefix");
    }

    public String byeSuffix() {
        return env.getProperty("messages.members.byeSuffix");
    }

    public String welcomeAdminPrefix() {
        return env.getProperty("messages.members.welcomeAdminPrefix");
    }

    public String welcomeAdminSuffix() {
        return env.getProperty("messages.members.welcomeAdminSuffix");
    }

    public String byeAdmin() {
        return env.getProperty("messages.members.byeAdmin");
    }

    //documents messages

    public String emptyQuestionList() {
        return env.getProperty("messages.documents.emptyQuestionList");
    }

    public String fromNotAdmin() {
        return env.getProperty("messages.documents.fromNotAdmin");
    }

    public String wrongDocumentType() {
        return env.getProperty("messages.documents.wrongDocumentType");
    }

    public String emptyQuestionsNotAdded() {
        return env.getProperty("messages.documents.emptyQuestionsNotAdded");
    }

    public String equalsQuestionsNotAdded() {
        return env.getProperty("messages.documents.equalsQuestionsNotAdded");
    }

    public String questionsAdded() {
        return env.getProperty("messages.documents.questionsAdded");
    }

    public String noOneQuestionAdded() {
        return env.getProperty("messages.documents.noOneQuestionAdded");
    }

    public String defaultQuestionGroup() {
        return env.getProperty("messages.documents.defaultQuestionGroup");
    }

    public String mimeType() {
        return env.getProperty("document.excel.mimeType");
    }

    //questions messages

    public String emptyList() {
        return env.getProperty("messages.questions.emptyList");
    }

    //games messages

    public String requestNewGameName() {
        return env.getProperty("messages.games.requestNewGameName");
    }

    public String requestQuestionsGroups() {
        return env.getProperty("messages.games.requestQuestionsGroups");
    }

    public String addedQuestionGroup() {
        return env.getProperty("messages.games.addedQuestionGroup");
    }

    public String requestMaxQuestionsCount() {
        return env.getProperty("messages.games.requestMaxQuestionsCount");
    }

    public String requestStartCountTasks() {
        return env.getProperty("messages.games.requestStartCountTasks");
    }

    public String requestMaxPerformedQuestionCount() {
        return env.getProperty("messages.games.requestMaxPerformedQuestionCount");
    }

    public String requestMinQuestionsCountInGame() {
        return env.getProperty("messages.games.requestMinQuestionsCountInGame");
    }

    public String requestQuestionsCountToAdd() {
        return env.getProperty("messages.games.requestQuestionsCountToAdd");
    }

    public String requestMaxTimeMinutes() {
        return env.getProperty("messages.games.requestMaxTimeMinutes");
    }

    public String gameAdded() {
        return env.getProperty("messages.games.gameAdded");
    }

    public String gameInfo() {
        return env.getProperty("messages.games.gameInfo");
    }

    public String nameAlreadyTaken() {
        return env.getProperty("messages.games.nameAlreadyTaken");
    }

    public String invalidNumber() {
        return env.getProperty("messages.games.invalidNumber");
    }

    public String invalidQuestionCount() {
        return env.getProperty("messages.games.invalidQuestionCount");
    }

    public String startQMoreMaxQ() {
        return env.getProperty("messages.games.startQMoreMaxQ");
    }

    public String maxPerformedQMoreMaxQ() {
        return env.getProperty("messages.games.maxPerformedQMoreMaxQ");
    }

    public String emptyGamesList() {
        return env.getProperty("messages.games.emptyGamesList");
    }

    public String failedDeletingGame() {
        return env.getProperty("messages.games.failedDeletingGame");
    }

    //-----------------API END-----------------

}

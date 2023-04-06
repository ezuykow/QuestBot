package ru.coffeecoders.questbot.commands.players;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Team;
import ru.coffeecoders.questbot.services.TeamService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PlayersCommandsActions {
    private final TeamService teamService;
    private final PlayersCommandsMsgSender msgSender;
    private final KeyboardFactory keyboardFactory;

    public PlayersCommandsActions(TeamService teamService, PlayersCommandsMsgSender msgSender, KeyboardFactory keyboardFactory) {
        this.teamService = teamService;
        this.msgSender = msgSender;
        this.keyboardFactory = keyboardFactory;
    }

    public void showScore(long chatId) {
        Map<String, Integer> score = new HashMap<>();
        teamService.findAll().forEach(a -> score.put(a.getTeamName(), a.getScore()));
        msgSender.showScore(chatId, score);
    }

    public void showTasks() {
        //TODO получить список вопросов и отправить в сендер
    }

    public void regTeam() {

    }

    public void joinTeam(long chatId) {
        List<String> teams = teamService.findAll().stream().map(Team::getTeamName).toList();
        msgSender.chooseYourTeam(chatId, teams);
        keyboardFactory.createKeyboard(chatId, teams);
    }
}

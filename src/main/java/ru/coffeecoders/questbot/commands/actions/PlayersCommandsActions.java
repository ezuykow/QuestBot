package ru.coffeecoders.questbot.commands.actions;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Task;
import ru.coffeecoders.questbot.entities.Team;
import ru.coffeecoders.questbot.msg.senders.MessageSender;
import ru.coffeecoders.questbot.services.GameService;
import ru.coffeecoders.questbot.services.QuestionService;
import ru.coffeecoders.questbot.services.TaskService;
import ru.coffeecoders.questbot.services.TeamService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author anna
 */
@Component
public class PlayersCommandsActions {
    private final TeamService teamService;
    private final TaskService taskService;
    private final GameService gameService;
    private final MessageSender msgSender;
    private final QuestionService questionService;
    private final KeyboardFactory keyboardFactory;
    private final Environment env;

    public PlayersCommandsActions(TeamService teamService,
                                  TaskService taskService,
                                  GameService gameService,
                                  MessageSender msgSender,
                                  QuestionService questionService,
                                  KeyboardFactory keyboardFactory,
                                  Environment env) {
        this.teamService = teamService;
        this.taskService = taskService;
        this.gameService = gameService;
        this.msgSender = msgSender;
        this.questionService = questionService;
        this.keyboardFactory = keyboardFactory;
        this.env = env;
    }

    /**
     * Собирает коллекцию (Map) из пар название команды-счет и передает ее
     * в {@link PlayersCommandsActions#createTextFromMap(Map)}, затем передает в сендер
     *
     * @param chatId id чата
     */
    public void showScores(long chatId) {
        Map<String, Integer> scores = new HashMap<String, Integer>();
        teamService.findAll().forEach(a -> scores.put(a.getTeamName(), a.getScore()));
        msgSender.send(chatId, createTextFromMap(scores));
    }

    /**
     * Получает название текущей игры, а затем список (List) вопросов к ней и передает
     * их в {@link PlayersCommandsActions#createTextFromList(List)}, затем передает в сендер
     *
     * @param chatId id чата
     */
    public void showTasks(long chatId) {
        //TODO добавить проверку, есть ли запущенная игра, чтоб не проверять больше ничего
        try {
            msgSender.send(chatId, createTextFromList(getAllTasksAsString()));
        } catch (IndexOutOfBoundsException exception) {
            msgSender.send(chatId, env.getProperty("messages.players.noCurrentGame"));
        }
    }

    /**
     * Отправляет в {@link  MessageSender} строку с предложением ввести название команды
     *
     * @param chatId id чата
     */
    public void regTeam(long chatId) {
        msgSender.send(chatId, env.getProperty("messages.players.enterTeamName"));
    }

    /**
     * Получает названия всех команд, и передает в {@link MessageSender}
     * клавиатуру с этими названиями
     *
     * @param chatId id чата
     */
    public void joinTeam(long chatId) {
        List<String> teams = teamService.findAll().stream().map(Team::getTeamName).toList();
        if (teams.isEmpty()) {
            msgSender.send(chatId, env.getProperty("messages.players.noTeamsRegisteredYet"));
        } else {
            msgSender.send(chatId, env.getProperty("messages.players.chooseYourTeam"),
                    keyboardFactory.createAllTeamsKeyboard(teams));
        }
    }

    private List<String> getAllTasksAsString() throws IndexOutOfBoundsException{
        List<Integer> tasksIds = taskService.findByGameName(gameService.findAll().get(0).getGameName())
                .stream().map(Task::getQuestionId);
        List<String> tasks = new ArrayList<>();
        tasksIds.forEach(a -> tasks.add(questionService.findById(a).get().getQuestion()));
        return tasks;
    }
    private String createTextFromMap(Map<String, Integer> map) {
        StringBuilder sb = new StringBuilder();
        map.forEach((name, score)-> sb.append(name).append(": ").append(score).append('\n'));
        return sb.toString();
    }

    private String createTextFromList(List<String> list) {
        StringBuilder sb = new StringBuilder();
        list.forEach(a -> sb.append("* ").append(a).append("\n"));
        return sb.toString();
    }
}

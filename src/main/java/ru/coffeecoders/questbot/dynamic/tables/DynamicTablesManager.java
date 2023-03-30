package ru.coffeecoders.questbot.dynamic.tables;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.dynamic.tables.creators.PlayersTableCreator;
import ru.coffeecoders.questbot.dynamic.tables.creators.TasksTableCreator;
import ru.coffeecoders.questbot.dynamic.tables.creators.TeamsTableCreator;

import java.util.Map;

@Component
public class DynamicTablesManager {

    @Value("${dynamic.tables.teams.namePostfix}")
    private String teamsTableNamePostfix;
    @Value("${dynamic.tables.players.namePostfix}")
    private String playersTableNamePostfix;
    @Value("${dynamic.tables.tasks.mapKey}")
    private String tasksKey;
    @Value("${dynamic.tables.teams.mapKey}")
    private String teamsKey;
    @Value("${dynamic.tables.players.mapKey}")
    private String playersKey;

    private final TasksTableCreator tasksCreator;
    private final TeamsTableCreator teamsCreator;
    private final PlayersTableCreator playersCreator;

    private Map<String, String> tablesNames;

    public DynamicTablesManager(TasksTableCreator tasksCreator, TeamsTableCreator teamsCreator, PlayersTableCreator playersCreator) {
        this.tasksCreator = tasksCreator;
        this.teamsCreator = teamsCreator;
        this.playersCreator = playersCreator;
    }

    public Map<String, String> createTables(String gameName, Long tgChatId) {

        createTablesNames(gameName, tgChatId);
        tasksCreator.createTable(tablesNames.get(tasksKey));
        teamsCreator.createTable(tablesNames.get(teamsKey));
        playersCreator.createTable(tablesNames.get(playersKey));

        return tablesNames;
    }

    private void createTablesNames(String gameName, Long tgChatId) {
        final String tasksTableName = gameName + "_" + tgChatId;
        final String teamsTableName = tasksTableName + teamsTableNamePostfix;
        final String playersTableName = tasksTableName + playersTableNamePostfix;

        tablesNames.put(tasksKey, tasksTableName);
        tablesNames.put(teamsKey, teamsTableName);
        tablesNames.put(playersKey, playersTableName);
    }
}

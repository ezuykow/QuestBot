package ru.coffeecoders.questbot.commands;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Commands {
    public enum AdminsCommands {
        NEWADMIN,
        NEWGAME,
        PLAYING,
        ADDQUESTION,
        DELETEQUESTION,
        EDITQUESTION,
        SHOWQUESTIONS,
        S
    }

    public enum PlayersCommands {
        START
    }

    public enum GlobalAdminsCommands {
        ADMIN_ON
    }

    public List<String> getAllAdminsCommands() {
        return Arrays.stream(Commands.AdminsCommands.values())
                .map(Commands.AdminsCommands::name)
                .toList();
    }

    public List<String> getAllGlobalAdminsCommands() {
        return Arrays.stream(Commands.GlobalAdminsCommands.values())
                .map(Commands.GlobalAdminsCommands::name)
                .toList();
    }

    public List<String> getAllPlayersCommands() {
        return Arrays.stream(Commands.PlayersCommands.values())
                .map(Commands.PlayersCommands::name)
                .toList();
    }
}

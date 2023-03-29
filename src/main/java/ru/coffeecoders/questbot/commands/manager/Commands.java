package ru.coffeecoders.questbot.commands.manager;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Commands {
    public enum AdminsCommands {
        NEW_ADMIN("newadmin"),
        NEW_GAME("newgame"),
        PLAYING("playing"),
        ADD_QUESTION("add"),
        DELETE_QUESTION("deletequestion"),
        EDIT_QUESTION("editquestion"),
        SHOW_QUESTIONS("showquestions"),
        MAIN_PAGE("s");

        private final String command;

        AdminsCommands(String command) {
            this.command = command;
        }

        public String getCommand() {
            return command;
        }
    }

    public enum PlayersCommands {
        START("start");
        private final String command;

        PlayersCommands(String command) {
            this.command = command;
        }

        public String getCommand() {
            return command;
        }
    }

    public enum GlobalAdminsCommands {
        ADMIN_ON("adminon");
        private final String command;

        GlobalAdminsCommands(String command) {
            this.command = command;
        }

        public String getCommand() {
            return command;
        }
    }

    public List<String> getAllAdminsCommands() {
        return Arrays.stream(Commands.AdminsCommands.values())
                .map(Commands.AdminsCommands::getCommand)
                .toList();
    }

    public List<String> getAllGlobalAdminsCommands() {
        return Arrays.stream(Commands.GlobalAdminsCommands.values())
                .map(Commands.GlobalAdminsCommands::getCommand)
                .toList();
    }

    public List<String> getAllPlayersCommands() {
        return Arrays.stream(Commands.PlayersCommands.values())
                .map(Commands.PlayersCommands::getCommand)
                .toList();
    }
}

package ru.coffeecoders.questbot.commands;

import org.springframework.stereotype.Component;

import static ru.coffeecoders.questbot.commands.Commands.Attribute.*;

@Component
public class Commands {
    public enum Command {
        START(GLOBALADMIN),

        SHOWQUESTIONS(ADMIN),

        SCORE(PLAYER),
        TASKS(PLAYER),
        REGTEAM(PLAYER),
        JOINTEAM(PLAYER);

        private final Attribute attribute;

        Command(Attribute attribute) {
            this.attribute = attribute;
        }

        public Attribute getAttribute() {
            return attribute;
        }
    }

    public enum Attribute {
        GLOBALADMIN,
        ADMIN,
        PLAYER
    }
}

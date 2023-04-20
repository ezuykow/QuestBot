package ru.coffeecoders.questbot.managers.commands;

import static ru.coffeecoders.questbot.managers.commands.Command.Attribute.*;

public enum Command {
    START(GLOBALADMIN),
    ADMINON(GLOBALADMIN),
    STOPBOT(GLOBALADMIN),

    SHOWQUESTIONS(ADMIN),
    ADMINOFF(ADMIN),

    SCORE(PLAYER),
    TASKS(PLAYER),
    REGTEAM(PLAYER),
    JOINTEAM(PLAYER),;

    public enum Attribute {
        GLOBALADMIN,
        ADMIN,
        PLAYER,
    }

    private final Attribute attribute;

    Command(Attribute attribute) {
        this.attribute = attribute;
    }
    public Attribute getAttribute() {
        return attribute;
    }
}

package ru.coffeecoders.questbot.managers.commands;

import static ru.coffeecoders.questbot.managers.commands.Command.Attribute.*;

public enum Command {
    STOPBOT(GLOBALADMIN),
    PREPAREGAME(GLOBALADMIN),
    STARTGAME(GLOBALADMIN),
    DROPPREPARE(GLOBALADMIN),
    DELETECHAT(GLOBALADMIN),

    SHOWGAMES(ADMIN),
    SHOWQUESTIONS(ADMIN),
    NEWGAME(ADMIN),

    START(OWNER),
    ADMINON(OWNER),
    ADMINOFF(OWNER),
    PROMOTE(OWNER),
    DEMOTE(OWNER),

    SCORE(PLAYER),
    TASKS(PLAYER),
    REGTEAM(PLAYER),
    JOINTEAM(PLAYER),;

    public enum Attribute {
        OWNER,
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

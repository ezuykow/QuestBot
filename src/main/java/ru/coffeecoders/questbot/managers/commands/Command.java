package ru.coffeecoders.questbot.managers.commands;

import static ru.coffeecoders.questbot.managers.commands.Command.Attribute.*;

public enum Command {

    PREPAREGAME(GLOBALADMIN),
    DROPPREPARE(GLOBALADMIN),
    STARTGAME(GLOBALADMIN),
    DROPGAME(GLOBALADMIN),
    DELETECHAT(GLOBALADMIN),
    QUESTIONS(GLOBALADMIN),
    INFO(GLOBALADMIN),
    REGTEAM(GLOBALADMIN),

    EMPTY(ADMIN),
    SHOWGAMES(ADMIN),
    SHOWQUESTIONS(ADMIN),
    NEWGAME(ADMIN),

    START(OWNER),
    ADMINON(OWNER),
    ADMINOFF(OWNER),
    PROMOTE(OWNER),
    DEMOTE(OWNER),
    PROPERTIES(OWNER),
    STOPBOT(OWNER);

    public static final String MY_COMMANDS = """
                ✏ /questions - во время проведения игры продублировать список актуальных вопросов
                ✏ /info - во время проведения игры показать оставшееся время и счет команд
                ✏ /preparegame - в игровом чате - запустить подготовку к игре
                ✏ /dropprepare - в игровом чате - прервать подготовку к игре
                ✏ /regteam - после /preparegame в игровом чате - указать количество команд на игру
                ✏ /startgame - в игровом чате - запустить подготовленную игру
                ✏ /dropgame - в игровом чате - прервать запущенную игру
                ✏ /deletechat - в игровом чате - удалить этот чат из системы
                ✏ /showgames - в админском чате - показать созданные игры
                ✏ /showquestions - в админском чате - показать все вопросы
                ✏ /newgame - в админском чате - создать новую игру
                ✏ /properties - (Владелец) в админском чате - показать изменяемые параметры бота
                ✏ /adminon - (Владелец) в игровом чате - сделать текущий чат администраторским
                ✏ /adminoff - (Владелец) в админском чате - сделать текущий чат не администраторским
                ✏ /promote - (Владелец) в админском чате - назначить нового администратора бота
                ✏ /demote - (Владелец) в админском чате - понизить одного из администраторов бота
                ✏ /stopbot - (Владелец) остановить бота
                """;


    public enum Attribute {
        OWNER,
        GLOBALADMIN,
        ADMIN,
    }

    private final Attribute attribute;

    Command(Attribute attribute) {
        this.attribute = attribute;
    }
    public Attribute getAttribute() {
        return attribute;
    }
}

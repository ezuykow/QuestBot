package ru.coffeecoders.questbot.commands;

import org.springframework.stereotype.Component;

@Component
public class Commands {
    public enum AllCommands {
        NEWADMIN(Attribute.ADMIN),
        NEWGAME(Attribute.ADMIN),
        PLAYING(Attribute.ADMIN),
        ADDQUESTION(Attribute.ADMIN),
        DELETEQUESTION(Attribute.ADMIN),
        EDITQUESTION(Attribute.ADMIN),
        SHOWQUESTIONS(Attribute.ADMIN),
        S(Attribute.ADMIN),
        ADMINON(Attribute.GLOBALADMIN),
        START(Attribute.PLAYER);

        private final Attribute attribute;

        AllCommands(Attribute attribute) {
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

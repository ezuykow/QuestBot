package ru.coffeecoders.questbot.commands;
import org.springframework.stereotype.Component;
import static ru.coffeecoders.questbot.commands.Commands.Attribute.*;

/**
 * @author anna
 */
@Component
public class Commands {
    public enum Command {
        NEWADMIN(ADMIN),
        NEWGAME(ADMIN),
        PLAYING(ADMIN),
        ADDQUESTION(ADMIN),
        DELETEQUESTION(ADMIN),
        EDITQUESTION(ADMIN),
        SHOWQUESTIONS(ADMIN),
        S(ADMIN),
        ADMINON(GLOBALADMIN),
        START(GLOBALADMIN),
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

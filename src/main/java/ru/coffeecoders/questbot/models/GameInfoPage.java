package ru.coffeecoders.questbot.models;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import ru.coffeecoders.questbot.actions.newgame.utils.NewGameUtils;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.keyboards.viewers.GameInfoKeyboard;

/**
 * @author ezuykow
 */
public class GameInfoPage {

    private String text;
    private final InlineKeyboardMarkup keyboard;

    private GameInfoPage(Game game, String prop, NewGameUtils utils) {
        createText(game, prop, utils);
        keyboard = GameInfoKeyboard.createKeyboard(game.getGameName());
    }

    //-----------------API START-----------------

    /**
     * Возвращает новый экземпляр {@link GameInfoPage}
     * @param game {@link Game} - игра, которую нужно отобразить
     * @param prop шаблон вывода
     * @param utils {@link NewGameUtils}
     * @return собранный {@link GameInfoPage}
     * @author ezuykow
     */
    public static GameInfoPage createPage(Game game, String prop, NewGameUtils utils) {
        return new GameInfoPage(game, prop, utils);
    }

    /**
     * @return {@code GameInfoPage.text} - текст страницы
     * @author ezuykow
     */
    public String getText() {
        return text;
    }

    /**
     * @return {@code GameInfoPage.keyboard} - клавиатуру страницы
     * @author ezuykow
     */
    public InlineKeyboardMarkup getKeyboard() {
        return keyboard;
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void createText(Game game, String prop, NewGameUtils utils) {
        text = String.format(prop,
                game.getGameName(),
                utils.getGroupsNamesMsg(game.getGroupsIds()),
                game.getMaxQuestionsCount(),
                game.getStartCountTasks(),
                game.getMaxPerformedQuestionsCount(),
                game.getMinQuestionsCountInGame(),
                game.getQuestionsCountToAdd(),
                game.getMaxTimeMinutes(),
                game.isAdditionWithTask()
                );
    }
}

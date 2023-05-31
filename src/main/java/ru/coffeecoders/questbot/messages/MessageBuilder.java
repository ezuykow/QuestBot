package ru.coffeecoders.questbot.messages;

import org.apache.commons.text.TextStringBuilder;
import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.actions.newgame.utils.NewGameUtils;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.entities.NewGameCreatingState;
import ru.coffeecoders.questbot.services.GameService;
import ru.coffeecoders.questbot.services.GlobalChatService;
import ru.coffeecoders.questbot.services.NewGameCreatingStateService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author ezuykow
 */
@Component
public class MessageBuilder {

    public static final String ESCAPES = """
            Следующие плейсхолдеры заменятся на соответствующие значения:
            
            \\n - перевод строки;
            \\gameName - имя связанной с выводимым сообщением игры;
            \\startCountTasks - стартовое количество вопросов связанной с выводимым сообщением игры;
            \\groups - группы связанной с выводимым сообщением игры;
            \\maxQuestionsCount - максимальное количество вопросов связанной с выводимым сообщением игры;
            \\maxPerformed - количество вопросов для досрочной победы связанной с выводимым сообщением игры;
            \\minQuestions - порог вопросов для добавления новых связанной с выводимым сообщением игры;
            \\questionsToAdd - количество добавляемых вопросов связанной с выводимым сообщением игры;
            \\time - максимальное время проведения связанной с выводимым сообщением игры;
            \\addsWithTasks - добавляется ли доп. информация к вопросу у связанной с выводимым сообщением игры;
            \\remainingTime - оставшееся время проведения связанной с выводимым сообщением игры;
            \\shuffle - перемешиваются ли вопросы в связанной с выводимым сообщением игре;
            """;



    private final GameService gameService;
    private final NewGameCreatingStateService newGameCreatingStateService;
    private final GlobalChatService globalChatService;
    private final NewGameUtils utils;

    public MessageBuilder(GameService gameService, NewGameCreatingStateService newGameCreatingStateService,
                          GlobalChatService globalChatService, NewGameUtils utils) {
        this.gameService = gameService;
        this.newGameCreatingStateService = newGameCreatingStateService;
        this.globalChatService = globalChatService;
        this.utils = utils;
    }

    //-----------------API START-----------------

    public String build(String originalMsg, long chatId) {
        Map<String, String> map = fillPlaceholdersAndValues(chatId);
        return buildString(map, originalMsg);
    }

    public String build(String originalMsg, long chatId, Game game) {
        Map<String, String> map = fillPlaceholdersAndValues(chatId, game);
        return buildString(map, originalMsg);
    }

    public String build(String originalMsg, long chatId, NewGameCreatingState state) {
        return build(originalMsg, chatId, new Game(state));
    }

    public String build(String originalMsg, Game game, GlobalChat chat) {
        Map<String, String> map = fillPlaceholdersAndValues(game, chat);
        return buildString(map, originalMsg);
    }

    //-----------------API END-----------------

    private String buildString(Map<String, String> map, String originalMsg) {
        TextStringBuilder sb = new TextStringBuilder(originalMsg);
        map.forEach(sb::replaceAll);
        return sb.toString();
    }

    private Map<String, String> fillPlaceholdersAndValues(long chatId) {
        final Optional<GlobalChat> chatOpt = globalChatService.findById(chatId);
        Map<String, String> map = new HashMap<>();
        putNonGameValues(map);

        final Optional<Game> targetGameOpt = getTargetGame(chatId, chatOpt);
        targetGameOpt.ifPresent(game -> putValuesForGame(map, game, chatOpt));

        return map;
    }

    private Map<String, String> fillPlaceholdersAndValues(long chatId, Game game) {
        final Optional<GlobalChat> chatOpt = globalChatService.findById(chatId);
        Map<String, String> map = new HashMap<>();
        putNonGameValues(map);

        putValuesForGame(map, game, chatOpt);
        return map;
    }

    private Map<String, String> fillPlaceholdersAndValues(Game game, GlobalChat chat) {
        Map<String, String> map = new HashMap<>();
        putNonGameValues(map);

        putValuesForGame(map, game, Optional.of(chat));
        return map;
    }

    private Optional<Game> getTargetGame(long chatId, Optional<GlobalChat> chatOpt) {
        if (chatOpt.isPresent()) {
            String gameName = chatOpt.get().getCreatingGameName();
            return gameService.findByName((gameName == null) ? "" : gameName);
        }
        return Optional.of(new Game(
                newGameCreatingStateService.findById(chatId).orElse(new NewGameCreatingState())));
    }

    private void putNonGameValues(Map<String, String> map) {
        map.put("\\n", "\n");
    }

    private void putValuesForGame(Map<String, String> map, Game game, Optional<GlobalChat> chatOpt) {
        map.put("\\gameName", game.getGameName());
        map.put("\\startCountTasks", String.valueOf(game.getStartCountTasks()));
        map.put("\\groups", utils.getGroupsNamesMsg(game.getGroupsIds()));
        map.put("\\maxQuestionsCount", String.valueOf(game.getMaxQuestionsCount()));
        map.put("\\maxPerformed", String.valueOf(game.getMaxPerformedQuestionsCount()));
        map.put("\\minQuestions", String.valueOf(game.getMinQuestionsCountInGame()));
        map.put("\\questionsToAdd", String.valueOf(game.getQuestionsCountToAdd()));
        map.put("\\time", String.valueOf(game.getMaxTimeMinutes()));
        map.put("\\addsWithTasks", String.valueOf(game.isAdditionWithTask()));
        map.put("\\shuffle", String.valueOf(game.isShuffleQuestions()));
        map.put("\\remainingTime", String.valueOf(
                chatOpt.map(chat -> game.getMaxTimeMinutes() - chat.getMinutesSinceStart())
                        .orElseGet(game::getMaxTimeMinutes)));
    }
}

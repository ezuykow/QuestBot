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
        TextStringBuilder sb = new TextStringBuilder(originalMsg);
        Map<String, String> placeholderAndValues = fillPlaceholdersAndValues(chatId);
        placeholderAndValues.forEach(sb::replaceAll);
        return sb.toString();
    }

    //-----------------API END-----------------

    private Map<String, String> fillPlaceholdersAndValues(long chatId) {
        Map<String, String> map = new HashMap<>();

        final Optional<GlobalChat> chatOpt = globalChatService.findById(chatId);
        final Optional<Game> targetGameOpt = getTargetGame(chatId, chatOpt);

        map.put("\\n", "\n");

        if (targetGameOpt.isPresent()) {
            final Game targetGame = targetGameOpt.get();
            map.put("\\gameName", targetGame.getGameName());
            map.put("\\startCountTasks", String.valueOf(targetGame.getStartCountTasks()));
            map.put("\\groups", utils.getGroupsNamesMsg(targetGame.getGroupsIds()));
            map.put("\\maxQuestionsCount", String.valueOf(targetGame.getMaxQuestionsCount()));
            map.put("\\maxPerformed", String.valueOf(targetGame.getMaxPerformedQuestionsCount()));
            map.put("\\minQuestions", String.valueOf(targetGame.getMinQuestionsCountInGame()));
            map.put("\\questionsToAdd", String.valueOf(targetGame.getQuestionsCountToAdd()));
            map.put("\\time", String.valueOf(targetGame.getMaxTimeMinutes()));
            map.put("\\addsWithTasks", String.valueOf(targetGame.isAdditionWithTask()));
            map.put("\\remainingTime", String.valueOf(
                    chatOpt.map(chat -> targetGame.getMaxTimeMinutes() - chat.getMinutesSinceStart())
                            .orElseGet(targetGame::getMaxTimeMinutes)));
        }

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
}

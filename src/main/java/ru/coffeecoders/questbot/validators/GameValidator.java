package ru.coffeecoders.questbot.validators;

import org.springframework.stereotype.Component;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.entities.GlobalChat;
import ru.coffeecoders.questbot.exceptions.NonExistentQuestionGroup;
import ru.coffeecoders.questbot.services.*;
import ru.coffeecoders.questbot.viewers.EndGameViewer;

/**
 * @author ezuykow
 */
@Component
public class GameValidator {

    private final GlobalChatService globalChatService;
    private final NewGameCreatingStateService newGameCreatingStateService;
    private final GameService gameService;
    private final QuestionGroupService questionGroupService;
    private final QuestionService questionService;
    private final EndGameViewer endGameViewer;

    public GameValidator(GlobalChatService globalChatService, NewGameCreatingStateService newGameCreatingStateService,
                         GameService gameService, QuestionGroupService questionGroupService, QuestionService questionService, EndGameViewer endGameViewer)
    {
        this.globalChatService = globalChatService;
        this.newGameCreatingStateService = newGameCreatingStateService;
        this.gameService = gameService;
        this.questionGroupService = questionGroupService;
        this.questionService = questionService;
        this.endGameViewer = endGameViewer;
    }

    //-----------------API START-----------------

    /**
     * @param chatId id чата
     * @return {@code true}, если в этом чате запущена игра, иначе {@code false}
     * @author ezuykow
     */
    public boolean isGameStarted(long chatId) {
        return globalChatService.findById(chatId)
                .filter(this::isGameStarted)
                .isPresent();
    }

    /**
     * @param chatId id чата
     * @return {@code true}, если в этом чате запущена подготовка к игре, иначе {@code false}
     * @author ezuykow
     */
    public boolean isGameCreating(long chatId) {
        return globalChatService.findById(chatId)
                .filter(this::isGameCreating)
                .isPresent();
    }

    /**
     * @param gameName название игры
     * @return {@code true}, если игра с таким названием подготавливается или запущена в любом чате,
     * иначе {@code false}
     * @author ezuykow
     */
    public boolean isGameCreating(String gameName) {
        return globalChatService.findAll().stream()
                .anyMatch(gc -> gc.getCreatingGameName() != null && gc.getCreatingGameName().equals(gameName));
    }

    /**
     * @param chatId id чата
     * @return {@code true}, если в этом чате создается новая игра, иначе {@code false}
     * @author ezuykow
     */
    public boolean isNewGameCreating(long chatId) {
        return newGameCreatingStateService.findById(chatId).isPresent();
    }

    /**
     * @param gameName название игры
     * @return {@code true}, если игра с таким названием уже существует или создается, иначе {@code false}
     * @author ezuykow
     */
    public boolean isGameNameAlreadyTaken(String gameName) {
        return gameService.findAll()
                .stream().anyMatch(g -> g.getGameName().equals(gameName))
                || newGameCreatingStateService.findAll()
                .stream().anyMatch(cg -> cg.getGameName() != null && cg.getGameName().equals(gameName));
    }

    /**
     * @param game проверяемая игра
     * @return {@code true}, если количество вопросов, подходящих по группам к игре не меньше, чем нужно для игры,
     * иначе {@code false}
     * @author ezuykow
     */
    public boolean isRightQuestionsCount(Game game) {
        int requestCount = game.getMaxQuestionsCount();
        int existentCount = 0;

        for (int groupId : game.getGroupsIds()) {
            if (groupId != 0) {
                String groupName = questionGroupService.findById(groupId)
                        .orElseThrow(NonExistentQuestionGroup::new).getGroupName();
                existentCount += questionService.findByGroupName(groupName).size();
            }
        }

        return requestCount <= existentCount;
    }

    /**
     * Проверяет запущенные игры в чатах. Если время со старта игры равно максимальному времени проведения - то
     * заканчивает игру
     * @author ezuykow
     */
    public boolean validateGamesTimeEnded(GlobalChat chat, int minsToEnd) {
        if (minsToEnd <= 0) {
            endGameViewer.finishGameByTimeEnded(chat);
            return true;
        }
        return false;
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private boolean isGameStarted(GlobalChat globalChat) {
        return globalChat.isGameStarted();
    }

    /**
     * @author ezuykow
     */
    private boolean isGameCreating(GlobalChat globalChat) {
        return globalChat.getCreatingGameName() != null
                && !isGameStarted(globalChat);
    }
}

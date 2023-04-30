package ru.coffeecoders.questbot.services;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.entities.Game;
import ru.coffeecoders.questbot.repositories.GameRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    Logger logger = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    /**
     *
     * @return
     *@author Anatoliy Shikin
     */
    public List<Game> findAll() {
        List<Game> list = gameRepository.findAll();
        logger.info("Games {} displaying", list.isEmpty() ? "are not" : "are");
        return list;
    }

    /**
     *
     * @param gameName
     * @return
     *@author Anatoliy Shikin
     */
    public Optional<Game> findByName(String gameName) {
        Optional<Game> game = gameRepository.findById(gameName);
        logger.info("Game {} with id = {}", game.isPresent() ? "found" : "not found", gameName);
        return game;
    }

    /**
     *
     * @param game
     * @return
     *@author Anatoliy Shikin
     */
    public Game save(Game game) {
        logger.info("Game = {} has been saved", game);
        return gameRepository.save(game);
    }

    /**
     * Сохраняет все игры из списка в БД
     * @param games список игр
     * @author ezuykow
     */
    public void saveAll(List<Game> games) {
        logger.info("Games = {} has been saved", games);
        gameRepository.saveAll(games);
    }

    /**
     * @author ezuykow
     */
    @Transactional
    public void deleteByGameName(String gameName) {
        logger.info("Game with name = {} has been deleted", gameName);
        gameRepository.deleteByGameName(gameName);
    }

    /**
     * Проверяет, что у игры в {@link Game#getGroupsIds} нет {@code groupId}, иначе меняет его на 0
     * @param groupId id удаленной группы
     * @author ezuykow
     */
    public void setGroupIdIfItsDeleted(int groupId) {
        logger.info("Setting group ID to 0 for all games with group ID = {}", groupId);
        List<Game> games = findAll();
        games.forEach(g -> swapGroupIdToZeroIfEquals(g, groupId));
        saveAll(games);
        logger.info("Group ID has been set to 0 for all games with group ID = {}", groupId);
    }

    //-----------------API END-----------------

    /**
     * @author ezuykow
     */
    private void swapGroupIdToZeroIfEquals(Game game, int groupId) {
        int[] ids = game.getGroupsIds();
        for (int i = 0; i < ids.length; i++) {
            if (ids[i] == groupId) {
                ids[i] = 0;
            }
        }
    }
}
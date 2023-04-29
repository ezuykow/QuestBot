package ru.coffeecoders.questbot.services;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.entities.Team;
import ru.coffeecoders.questbot.repositories.TeamRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    Logger logger = LoggerFactory.getLogger(TeamService.class);

    private final TeamRepository repository;

    public TeamService(TeamRepository repository) {
        this.repository = repository;
    }

    public List<Team> findAll() {
        List<Team> list = repository.findAll();
        logger.info("Teams are {} displaying", list.isEmpty() ? "not" : "");
        return list;
    }

    public Optional<Team> findByTeamName(String teamName) {
        Optional<Team> optional = repository.findById(teamName);
        logger.info("Team with teamName = {} {} found", teamName, optional.isPresent() ? "" : "not");
        return optional;
    }

    /**
     * @author ezuykow
     */
    public List<Team> findByChatId(long chatId) {
        return repository.findByChatId(chatId);
    }

    /**
     * @author ezuykow
     */
    public Optional<Team> findByChatIdAndTeamName(long chatId, String teamName) {
        return findByChatId(chatId).stream().filter(t -> t.getTeamName().equals(teamName)).findAny();
    }

    public Team save(Team team) {
        logger.info("Team = {} has been saved", team);
        return repository.save(team);
    }

    /**
     * @author ezuykow
     */
    @Transactional
    public void deleteAllByChatId(long chatId) {
        repository.deleteAllByChatId(chatId);
    }
}
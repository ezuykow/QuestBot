package ru.coffeecoders.questbot.services;

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
    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> findAll() {
        List<Team> list = teamRepository.findAll();
        logger.info("Teams {} displaying", list.isEmpty() ? "are not" : "are");
        return list;
    }

    public Optional<Team> findByTeamName(String teamName) {
        return teamRepository.findByTeamName(teamName);
    }

    public Team save(Team team) {
        logger.info("Team = {} has been saved", team);
        return teamRepository.save(team);
    }
}

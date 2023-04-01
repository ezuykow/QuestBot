package ru.coffeecoders.questbot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.coffeecoders.questbot.entities.Team;
import ru.coffeecoders.questbot.repositories.TeamRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TeamService {

    Logger logger = LoggerFactory.getLogger(TeamService.class);
    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> findAll() {
        List<Team> teamList = teamRepository.findAll();
        if (!teamList.isEmpty()) {
            logger.info("Teams are displayed");
        } else {
            logger.warn("No teams found");
        }
        return teamList;
    }

    public Optional<Team> findByTeamName(String teamName) {
        Optional<Team> optionalTeam = teamRepository.findByTeamName(teamName);
        return Optional.ofNullable(optionalTeam
                .orElseThrow(() -> {
                    logger.warn("Team not found with teamName = {}", teamName);
                    throw new NoSuchElementException("Team not found with teamName" + teamName);
                }));
    }

    public Team save(Team team) {
        logger.info("Team = {} has been saved", team);
        return teamRepository.save(team);
    }
}

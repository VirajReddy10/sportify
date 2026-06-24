package com.viraj.sportify.service;

import com.viraj.sportify.integration.TheSportsDbClient;
import com.viraj.sportify.integration.TheSportsDbPlayer;
import com.viraj.sportify.integration.TheSportsDbTeam;
import com.viraj.sportify.model.Player;
import com.viraj.sportify.model.Sport;
import com.viraj.sportify.model.Team;
import com.viraj.sportify.repository.PlayerRepository;
import com.viraj.sportify.repository.SportRepository;
import com.viraj.sportify.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SportsDataIngestionService {

    private final TheSportsDbClient client;
    private final SportRepository sportRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public SportsDataIngestionService(
            TheSportsDbClient client,
            SportRepository sportRepository,
            TeamRepository teamRepository,
            PlayerRepository playerRepository
    ) {
        this.client = client;
        this.sportRepository = sportRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    @Transactional
    public IngestionResult ingestLeague(String leagueName) {
        var teamsResponse = client.searchTeamsByLeague(leagueName);

        int teamsCreated = 0;
        int playersCreated = 0;

        if (teamsResponse == null || teamsResponse.teams() == null) {
            return new IngestionResult(0, 0);
        }

        for (TheSportsDbTeam externalTeam : teamsResponse.teams()) {
            Sport sport = findOrCreateSport(externalTeam.strSport());

            boolean teamAlreadyExisted = teamRepository.findByNameAndSport(externalTeam.strTeam(), sport).isPresent();
            Team team = findOrCreateTeam(externalTeam.strTeam(), sport);
            if (team.getId() == null) {
                teamsCreated++;
            }

            playersCreated += ingestPlayersForTeam(externalTeam.idTeam(), team);
        }

        return new IngestionResult(teamsCreated, playersCreated);
    }

    private Sport findOrCreateSport(String sportName) {
        return sportRepository.findByName(sportName)
                .orElseGet(() -> sportRepository.save(new Sport(sportName)));
    }

    private Team findOrCreateTeam(String teamName, Sport sport) {
        return teamRepository.findByNameAndSport(teamName, sport)
                .orElseGet(() -> teamRepository.save(new Team(teamName, sport)));
    }

    private int ingestPlayersForTeam(String externalTeamId, Team team) {
        var playersResponse = client.listPlayersByTeam(externalTeamId);
        if (playersResponse == null || playersResponse.player() == null) {
            return 0;
        }

        int created = 0;
        for (TheSportsDbPlayer externalPlayer : playersResponse.player()) {
            if (!"Active".equals(externalPlayer.strStatus())) {
                continue;
            }
            if (playerRepository.existsByNameAndTeam(externalPlayer.strPlayer(), team)) {
                continue;
            }
            Player player = new Player(externalPlayer.strPlayer(), externalPlayer.strPosition(), team);
            playerRepository.save(player);
            created++;
        }
        return created;
    }

    public record IngestionResult(int teamsCreated, int playersCreated) {
    }
}

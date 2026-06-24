package com.viraj.sportify.service;

import com.viraj.sportify.dto.GameRequest;
import com.viraj.sportify.dto.GameResponse;
import com.viraj.sportify.model.Game;
import com.viraj.sportify.model.Sport;
import com.viraj.sportify.model.Team;
import com.viraj.sportify.repository.GameRepository;
import com.viraj.sportify.repository.SportRepository;
import com.viraj.sportify.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final SportRepository sportRepository;
    private final TeamRepository teamRepository;

    public GameService(GameRepository gameRepository, SportRepository sportRepository, TeamRepository teamRepository) {
        this.gameRepository = gameRepository;
        this.sportRepository = sportRepository;
        this.teamRepository = teamRepository;
    }

    @Transactional(readOnly = true)
    public List<GameResponse> findAll() {
        return gameRepository.findAll().stream()
                .map(GameResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public GameResponse findById(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Game not found: " + id));
        return GameResponse.from(game);
    }

    @Transactional
    public GameResponse create(GameRequest request) {
        Sport sport = sportRepository.findById(request.sportId())
                .orElseThrow(() -> new IllegalArgumentException("Sport not found: " + request.sportId()));
        Team homeTeam = teamRepository.findById(request.homeTeamId())
                .orElseThrow(() -> new IllegalArgumentException("Home team not found: " + request.homeTeamId()));
        Team awayTeam = teamRepository.findById(request.awayTeamId())
                .orElseThrow(() -> new IllegalArgumentException("Away team not found: " + request.awayTeamId()));

        if (homeTeam.getId().equals(awayTeam.getId())) {
            throw new IllegalArgumentException("Home team and away team cannot be the same");
        }

        Game game = new Game(sport, homeTeam, awayTeam, request.gameDate());
        gameRepository.save(game);
        return GameResponse.from(game);
    }

    @Transactional
    public GameResponse updateScore(Long id, Integer homeScore, Integer awayScore) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Game not found: " + id));
        game.setHomeScore(homeScore);
        game.setAwayScore(awayScore);
        return GameResponse.from(game);
    }

    public void delete(Long id) {
        gameRepository.deleteById(id);
    }
}

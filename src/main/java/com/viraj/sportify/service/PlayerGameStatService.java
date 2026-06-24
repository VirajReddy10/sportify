package com.viraj.sportify.service;

import com.viraj.sportify.dto.PlayerGameStatRequest;
import com.viraj.sportify.dto.PlayerGameStatResponse;
import com.viraj.sportify.model.Game;
import com.viraj.sportify.model.Player;
import com.viraj.sportify.model.PlayerGameStat;
import com.viraj.sportify.repository.GameRepository;
import com.viraj.sportify.repository.PlayerGameStatRepository;
import com.viraj.sportify.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlayerGameStatService {

    private final PlayerGameStatRepository playerGameStatRepository;
    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;

    public PlayerGameStatService(
            PlayerGameStatRepository playerGameStatRepository,
            PlayerRepository playerRepository,
            GameRepository gameRepository
    ) {
        this.playerGameStatRepository = playerGameStatRepository;
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
    }

    @Transactional(readOnly = true)
    public List<PlayerGameStatResponse> findByGame(Long gameId) {
        return playerGameStatRepository.findByGameId(gameId).stream()
                .map(PlayerGameStatResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PlayerGameStatResponse> findByPlayer(Long playerId) {
        return playerGameStatRepository.findByPlayerId(playerId).stream()
                .map(PlayerGameStatResponse::from)
                .toList();
    }

    @Transactional
    public PlayerGameStatResponse create(PlayerGameStatRequest request) {
        Player player = playerRepository.findById(request.playerId())
                .orElseThrow(() -> new IllegalArgumentException("Player not found: " + request.playerId()));
        Game game = gameRepository.findById(request.gameId())
                .orElseThrow(() -> new IllegalArgumentException("Game not found: " + request.gameId()));

        PlayerGameStat stat = new PlayerGameStat(player, game);
        stat.setStats(request.stats());
        playerGameStatRepository.save(stat);
        return PlayerGameStatResponse.from(stat);
    }
}

package com.viraj.sportify.service;

import com.viraj.sportify.dto.PlayerRequest;
import com.viraj.sportify.dto.PlayerResponse;
import com.viraj.sportify.model.Player;
import com.viraj.sportify.model.Team;
import com.viraj.sportify.repository.PlayerRepository;
import com.viraj.sportify.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public PlayerService(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    @Transactional(readOnly = true)
    public List<PlayerResponse> findAll() {
        return playerRepository.findAll().stream()
                .map(PlayerResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PlayerResponse findById(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Player not found: " + id));
        return PlayerResponse.from(player);
    }

    @Transactional
    public PlayerResponse create(PlayerRequest request) {
        Team team = teamRepository.findById(request.teamId())
                .orElseThrow(() -> new IllegalArgumentException("Team not found: " + request.teamId()));

        Player player = new Player(request.name(), request.position(), team);
        playerRepository.save(player);
        return PlayerResponse.from(player);
    }

    public void delete(Long id) {
        playerRepository.deleteById(id);
    }
}

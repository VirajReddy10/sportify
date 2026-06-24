package com.viraj.sportify.controller;

import com.viraj.sportify.dto.PlayerGameStatRequest;
import com.viraj.sportify.dto.PlayerGameStatResponse;
import com.viraj.sportify.service.PlayerGameStatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/player-game-stats")
public class PlayerGameStatController {

    private final PlayerGameStatService playerGameStatService;

    public PlayerGameStatController(PlayerGameStatService playerGameStatService) {
        this.playerGameStatService = playerGameStatService;
    }

    @GetMapping("/game/{gameId}")
    public List<PlayerGameStatResponse> findByGame(@PathVariable Long gameId) {
        return playerGameStatService.findByGame(gameId);
    }

    @GetMapping("/player/{playerId}")
    public List<PlayerGameStatResponse> findByPlayer(@PathVariable Long playerId) {
        return playerGameStatService.findByPlayer(playerId);
    }

    @PostMapping
    public ResponseEntity<PlayerGameStatResponse> create(@Valid @RequestBody PlayerGameStatRequest request) {
        PlayerGameStatResponse response = playerGameStatService.create(request);
        return ResponseEntity.ok(response);
    }
}

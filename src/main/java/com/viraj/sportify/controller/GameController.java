package com.viraj.sportify.controller;

import com.viraj.sportify.dto.GameRequest;
import com.viraj.sportify.dto.GameResponse;
import com.viraj.sportify.dto.ScoreUpdateRequest;
import com.viraj.sportify.service.GameService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public List<GameResponse> findAll() {
        return gameService.findAll();
    }

    @GetMapping("/{id}")
    public GameResponse findById(@PathVariable Long id) {
        return gameService.findById(id);
    }

    @PostMapping
    public ResponseEntity<GameResponse> create(@Valid @RequestBody GameRequest request) {
        GameResponse response = gameService.create(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/score")
    public GameResponse updateScore(@PathVariable Long id, @Valid @RequestBody ScoreUpdateRequest request) {
        return gameService.updateScore(id, request.homeScore(), request.awayScore());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gameService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

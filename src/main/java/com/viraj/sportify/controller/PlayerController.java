package com.viraj.sportify.controller;

import com.viraj.sportify.dto.PlayerRequest;
import com.viraj.sportify.dto.PlayerResponse;
import com.viraj.sportify.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public List<PlayerResponse> findAll() {
        return playerService.findAll();
    }

    @GetMapping("/{id}")
    public PlayerResponse findById(@PathVariable Long id) {
        return playerService.findById(id);
    }

    @PostMapping
    public ResponseEntity<PlayerResponse> create(@Valid @RequestBody PlayerRequest request) {
        PlayerResponse response = playerService.create(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        playerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

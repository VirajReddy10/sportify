package com.viraj.sportify.controller;

import com.viraj.sportify.dto.TeamRequest;
import com.viraj.sportify.dto.TeamResponse;
import com.viraj.sportify.service.TeamService;
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
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public List<TeamResponse> findAll() {
        return teamService.findAll();
    }

    @GetMapping("/{id}")
    public TeamResponse findById(@PathVariable Long id) {
        return teamService.findById(id);
    }

    @PostMapping
    public ResponseEntity<TeamResponse> create(@Valid @RequestBody TeamRequest request) {
        TeamResponse response = teamService.create(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teamService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

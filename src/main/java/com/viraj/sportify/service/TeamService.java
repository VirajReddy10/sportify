package com.viraj.sportify.service;

import com.viraj.sportify.dto.TeamRequest;
import com.viraj.sportify.dto.TeamResponse;
import com.viraj.sportify.model.Sport;
import com.viraj.sportify.model.Team;
import com.viraj.sportify.repository.SportRepository;
import com.viraj.sportify.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final SportRepository sportRepository;

    public TeamService(TeamRepository teamRepository, SportRepository sportRepository) {
        this.teamRepository = teamRepository;
        this.sportRepository = sportRepository;
    }

    @Transactional(readOnly = true)
    public List<TeamResponse> findAll() {
        return teamRepository.findAll().stream()
                .map(TeamResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public TeamResponse findById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Team not found: " + id));
        return TeamResponse.from(team);
    }

    @Transactional
    public TeamResponse create(TeamRequest request) {
        Sport sport = sportRepository.findById(request.sportId())
                .orElseThrow(() -> new IllegalArgumentException("Sport not found: " + request.sportId()));

        Team team = new Team(request.name(), sport);
        teamRepository.save(team);
        return TeamResponse.from(team);
    }

    public void delete(Long id) {
        teamRepository.deleteById(id);
    }
}

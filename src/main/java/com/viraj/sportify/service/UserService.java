package com.viraj.sportify.service;

import com.viraj.sportify.dto.TeamResponse;
import com.viraj.sportify.model.Team;
import com.viraj.sportify.model.User;
import com.viraj.sportify.repository.TeamRepository;
import com.viraj.sportify.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public UserService(UserRepository userRepository, TeamRepository teamRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    @Transactional(readOnly = true)
    public List<TeamResponse> getFavoriteTeams(String username) {
        User user = findUser(username);
        return user.getFavoriteTeams().stream()
                .map(TeamResponse::from)
                .toList();
    }

    @Transactional
    public List<TeamResponse> addFavoriteTeam(String username, Long teamId) {
        User user = findUser(username);
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found: " + teamId));
        user.addFavoriteTeam(team);
        userRepository.save(user);
        return user.getFavoriteTeams().stream()
                .map(TeamResponse::from)
                .toList();
    }

    @Transactional
    public List<TeamResponse> removeFavoriteTeam(String username, Long teamId) {
        User user = findUser(username);
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found: " + teamId));
        user.removeFavoriteTeam(team);
        userRepository.save(user);
        return user.getFavoriteTeams().stream()
                .map(TeamResponse::from)
                .toList();
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }
}

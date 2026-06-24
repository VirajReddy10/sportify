package com.viraj.sportify.controller;

import com.viraj.sportify.dto.TeamResponse;
import com.viraj.sportify.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users/me/favorite-teams")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<TeamResponse> getFavoriteTeams(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.getFavoriteTeams(userDetails.getUsername());
    }

    @PostMapping("/{teamId}")
    public List<TeamResponse> addFavoriteTeam(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long teamId) {
        return userService.addFavoriteTeam(userDetails.getUsername(), teamId);
    }

    @DeleteMapping("/{teamId}")
    public List<TeamResponse> removeFavoriteTeam(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long teamId) {
        return userService.removeFavoriteTeam(userDetails.getUsername(), teamId);
    }
}

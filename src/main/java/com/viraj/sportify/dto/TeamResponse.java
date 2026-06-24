package com.viraj.sportify.dto;

import com.viraj.sportify.model.Team;

public record TeamResponse(Long id, String name, Long sportId, String sportName) {
    public static TeamResponse from(Team team) {
        return new TeamResponse(
                team.getId(),
                team.getName(),
                team.getSport().getId(),
                team.getSport().getName()
        );
    }
}

package com.viraj.sportify.dto;

import com.viraj.sportify.model.Player;

public record PlayerResponse(Long id, String name, String position, Long teamId, String teamName) {
    public static PlayerResponse from(Player player) {
        return new PlayerResponse(
                player.getId(),
                player.getName(),
                player.getPosition(),
                player.getTeam().getId(),
                player.getTeam().getName()
        );
    }
}

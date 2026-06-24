package com.viraj.sportify.dto;

import com.viraj.sportify.model.PlayerGameStat;

import java.util.Map;

public record PlayerGameStatResponse(
        Long id,
        Long playerId,
        String playerName,
        Long gameId,
        Map<String, Object> stats
) {
    public static PlayerGameStatResponse from(PlayerGameStat stat) {
        return new PlayerGameStatResponse(
                stat.getId(),
                stat.getPlayer().getId(),
                stat.getPlayer().getName(),
                stat.getGame().getId(),
                stat.getStats()
        );
    }
}

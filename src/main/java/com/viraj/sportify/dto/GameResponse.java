package com.viraj.sportify.dto;

import com.viraj.sportify.model.Game;

import java.time.LocalDateTime;

public record GameResponse(
        Long id,
        String sportName,
        Long homeTeamId,
        String homeTeamName,
        Long awayTeamId,
        String awayTeamName,
        LocalDateTime gameDate,
        Integer homeScore,
        Integer awayScore
) {
    public static GameResponse from(Game game) {
        return new GameResponse(
                game.getId(),
                game.getSport().getName(),
                game.getHomeTeam().getId(),
                game.getHomeTeam().getName(),
                game.getAwayTeam().getId(),
                game.getAwayTeam().getName(),
                game.getGameDate(),
                game.getHomeScore(),
                game.getAwayScore()
        );
    }
}

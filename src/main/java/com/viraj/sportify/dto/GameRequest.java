package com.viraj.sportify.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record GameRequest(
        @NotNull Long sportId,
        @NotNull Long homeTeamId,
        @NotNull Long awayTeamId,
        @NotNull LocalDateTime gameDate
) {
}

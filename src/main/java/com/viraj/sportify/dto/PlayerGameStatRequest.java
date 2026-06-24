package com.viraj.sportify.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record PlayerGameStatRequest(
        @NotNull Long playerId,
        @NotNull Long gameId,
        @NotNull Map<String, Object> stats
) {
}

package com.viraj.sportify.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ScoreUpdateRequest(
        @NotNull @PositiveOrZero Integer homeScore,
        @NotNull @PositiveOrZero Integer awayScore
) {
}

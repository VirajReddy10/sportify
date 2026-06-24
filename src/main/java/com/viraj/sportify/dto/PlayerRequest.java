package com.viraj.sportify.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PlayerRequest(
        @NotBlank String name,
        String position,
        @NotNull Long teamId
) {
}

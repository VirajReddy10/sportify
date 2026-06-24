package com.viraj.sportify.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TeamRequest(
        @NotBlank String name,
        @NotNull Long sportId
) {
}

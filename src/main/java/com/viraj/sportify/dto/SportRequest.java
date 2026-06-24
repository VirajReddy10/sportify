package com.viraj.sportify.dto;

import jakarta.validation.constraints.NotBlank;

public record SportRequest(@NotBlank String name) {
}

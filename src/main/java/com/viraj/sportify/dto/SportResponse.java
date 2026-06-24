package com.viraj.sportify.dto;

import com.viraj.sportify.model.Sport;

public record SportResponse(Long id, String name) {
    public static SportResponse from(Sport sport) {
        return new SportResponse(sport.getId(), sport.getName());
    }
}

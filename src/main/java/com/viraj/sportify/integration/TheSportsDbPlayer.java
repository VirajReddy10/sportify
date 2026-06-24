package com.viraj.sportify.integration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TheSportsDbPlayer(
        String idPlayer,
        String strPlayer,
        String strPosition,
        String strStatus
) {
}

package com.viraj.sportify.integration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TheSportsDbTeam(
        String idTeam,
        String strTeam,
        String strSport,
        String strLeague
) {
}

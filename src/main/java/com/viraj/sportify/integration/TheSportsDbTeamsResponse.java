package com.viraj.sportify.integration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TheSportsDbTeamsResponse(List<TheSportsDbTeam> teams) {
}

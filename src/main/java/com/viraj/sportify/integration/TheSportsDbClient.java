package com.viraj.sportify.integration;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class TheSportsDbClient {

    private static final String BASE_URL = "https://www.thesportsdb.com/api/v1/json/123";

    private final RestClient restClient;

    public TheSportsDbClient() {
        this.restClient = RestClient.create(BASE_URL);
    }

    public TheSportsDbTeamsResponse searchTeamsByLeague(String leagueName) {
        return restClient.get()
                .uri("/search_all_teams.php?l={league}", leagueName)
                .retrieve()
                .body(TheSportsDbTeamsResponse.class);
    }

    public TheSportsDbPlayersResponse listPlayersByTeam(String teamId) {
        return restClient.get()
                .uri("/lookup_all_players.php?id={teamId}", teamId)
                .retrieve()
                .body(TheSportsDbPlayersResponse.class);
    }
}

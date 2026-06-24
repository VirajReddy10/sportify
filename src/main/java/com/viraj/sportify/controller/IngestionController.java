package com.viraj.sportify.controller;

import com.viraj.sportify.dto.IngestionResponse;
import com.viraj.sportify.service.SportsDataIngestionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/ingestion")
public class IngestionController {

    private final SportsDataIngestionService ingestionService;

    public IngestionController(SportsDataIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/league")
    public IngestionResponse ingestLeague(@RequestParam String league) {
        var result = ingestionService.ingestLeague(league);
        return new IngestionResponse(result.teamsCreated(), result.playersCreated());
    }
}

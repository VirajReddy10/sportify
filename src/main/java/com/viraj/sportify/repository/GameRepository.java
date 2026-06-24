package com.viraj.sportify.repository;

import com.viraj.sportify.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findBySportId(Long sportId);
    List<Game> findByHomeTeamIdOrAwayTeamId(Long homeTeamId, Long awayTeamId);
}

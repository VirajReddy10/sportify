package com.viraj.sportify.repository;

import com.viraj.sportify.model.PlayerGameStat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerGameStatRepository extends JpaRepository<PlayerGameStat, Long> {
    List<PlayerGameStat> findByGameId(Long gameId);
    List<PlayerGameStat> findByPlayerId(Long playerId);
}

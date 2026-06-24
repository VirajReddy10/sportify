package com.viraj.sportify.repository;

import com.viraj.sportify.model.Player;
import com.viraj.sportify.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByTeamId(Long teamId);
    boolean existsByNameAndTeam(String name, Team team);
}

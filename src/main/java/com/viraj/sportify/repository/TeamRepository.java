package com.viraj.sportify.repository;

import com.viraj.sportify.model.Sport;
import com.viraj.sportify.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findBySportId(Long sportId);
    Optional<Team> findByNameAndSport(String name, Sport sport);
}

package com.viraj.sportify.repository;

import com.viraj.sportify.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findBySportId(Long sportId);
}

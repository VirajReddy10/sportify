package com.viraj.sportify.repository;

import com.viraj.sportify.model.Sport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SportRepository extends JpaRepository<Sport, Long> {
}

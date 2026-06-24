package com.viraj.sportify.service;

import com.viraj.sportify.dto.SportRequest;
import com.viraj.sportify.dto.SportResponse;
import com.viraj.sportify.model.Sport;
import com.viraj.sportify.repository.SportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SportService {

    private final SportRepository sportRepository;

    public SportService(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
    }

    public List<SportResponse> findAll() {
        return sportRepository.findAll().stream()
                .map(SportResponse::from)
                .toList();
    }

    public SportResponse findById(Long id) {
        Sport sport = sportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sport not found: " + id));
        return SportResponse.from(sport);
    }

    public SportResponse create(SportRequest request) {
        Sport sport = new Sport(request.name());
        sportRepository.save(sport);
        return SportResponse.from(sport);
    }

    public void delete(Long id) {
        sportRepository.deleteById(id);
    }
}

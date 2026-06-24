package com.viraj.sportify.controller;

import com.viraj.sportify.dto.SportRequest;
import com.viraj.sportify.dto.SportResponse;
import com.viraj.sportify.service.SportService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sports")
public class SportController {

    private final SportService sportService;

    public SportController(SportService sportService) {
        this.sportService = sportService;
    }

    @GetMapping
    public List<SportResponse> findAll() {
        return sportService.findAll();
    }

    @GetMapping("/{id}")
    public SportResponse findById(@PathVariable Long id) {
        return sportService.findById(id);
    }

    @PostMapping
    public ResponseEntity<SportResponse> create(@Valid @RequestBody SportRequest request) {
        SportResponse response = sportService.create(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sportService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package com.tap.controllers;

import com.tap.dto.ProficiencyLevelDto;
import com.tap.services.ProficiencyLevelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proficiency-levels")
public class ProficiencyLevelController {

    private final ProficiencyLevelService service;

    public ProficiencyLevelController(ProficiencyLevelService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProficiencyLevelDto> createLevel(@RequestBody ProficiencyLevelDto dto) {
        return ResponseEntity.ok(service.createLevel(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProficiencyLevelDto>> getAllLevels() {
        return ResponseEntity.ok(service.getAllLevels());
    }

    @GetMapping("/{levelId}")
    public ResponseEntity<ProficiencyLevelDto> getLevelById(@PathVariable Integer levelId) {
        return ResponseEntity.ok(service.getLevelById(levelId));
    }

    @DeleteMapping("/{levelId}")
    public ResponseEntity<Void> deleteLevel(@PathVariable Integer levelId) {
        service.deleteLevel(levelId);
        return ResponseEntity.noContent().build();
    }
}
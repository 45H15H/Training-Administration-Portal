package com.tap.services;

import com.tap.dto.ProficiencyLevelDto;
import com.tap.entities.ProficiencyLevel;
import com.tap.mappers.ProficiencyLevelMapper;
import com.tap.repositories.ProficiencyLevelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProficiencyLevelService {

    private final ProficiencyLevelRepository repository;
    private final ProficiencyLevelMapper mapper;

    public ProficiencyLevelService(ProficiencyLevelRepository repository, ProficiencyLevelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ProficiencyLevelDto createLevel(ProficiencyLevelDto dto) {
        ProficiencyLevel level = mapper.toEntity(dto);
        ProficiencyLevel saved = repository.save(level);
        return mapper.toDto(saved);
    }

    public List<ProficiencyLevelDto> getAllLevels() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public ProficiencyLevelDto getLevelById(Integer levelId) {
        ProficiencyLevel level = repository.findById(levelId)
                .orElseThrow(() -> new RuntimeException("Level not found"));
        return mapper.toDto(level);
    }

    public void deleteLevel(Integer levelId) {
        repository.deleteById(levelId);
    }
}
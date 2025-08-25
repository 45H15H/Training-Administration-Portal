package com.tap.repositories;

import com.tap.entities.ProficiencyLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProficiencyLevelRepository extends JpaRepository<ProficiencyLevel, Integer> {
    Optional<ProficiencyLevel> findByLevelName(ProficiencyLevel.LevelName levelName);
}
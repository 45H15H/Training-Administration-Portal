package com.tap.mappers;

import com.tap.dto.ProficiencyLevelDto;
import com.tap.entities.ProficiencyLevel;
import org.springframework.stereotype.Component;

@Component
public class ProficiencyLevelMapper {

    public ProficiencyLevel toEntity(ProficiencyLevelDto dto) {
        ProficiencyLevel level = new ProficiencyLevel();
        level.setLevelId(dto.getLevelId());
        level.setLevelName(ProficiencyLevel.LevelName.valueOf(dto.getLevelName().toLowerCase()));
        return level;
    }

    public ProficiencyLevelDto toDto(ProficiencyLevel entity) {
        ProficiencyLevelDto dto = new ProficiencyLevelDto();
        dto.setLevelId(entity.getLevelId());
        dto.setLevelName(entity.getLevelName().name());
        return dto;
    }
}
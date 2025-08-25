package com.tap.dto;

import lombok.Data;

@Data
public class ProficiencyLevelDto {
    private Integer levelId;
    private String levelName; // "beginner", "intermediate", "advanced"
}
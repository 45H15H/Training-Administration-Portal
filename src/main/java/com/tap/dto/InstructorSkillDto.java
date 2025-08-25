package com.tap.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class InstructorSkillDto {
    private UUID instructorId;
    private Integer levelId;
    private String skillName;
}
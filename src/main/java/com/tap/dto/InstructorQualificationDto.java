package com.tap.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class InstructorQualificationDto {
    private UUID instructorId;
    private String bio;
    private String highestQualification;
    private Integer relevantExperience;
}
package com.tap.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class InstructorResumeDto {
    private Integer resumeId;
    private UUID instructorId;
    private String resumeUrl;
    private LocalDateTime uploadedAt;
}

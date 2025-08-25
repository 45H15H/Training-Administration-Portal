package com.tap.dto;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import com.tap.dto.InstructorTimeSlotDto;

public record CourseDto(
        UUID courseId,
        UUID instructorId,
        String title,
        String description,
        Integer skillId,
        String skillName,
        BigDecimal price,
        Duration duration,
        Integer levelId,
        String levelName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Boolean isPublished,
        List<InstructorTimeSlotDto> timeSlots
) {}

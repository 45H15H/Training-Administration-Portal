package com.tap.dto;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

public record CourseCreationDto(
        UUID instructorId,
        String title,
        String description,
        Integer skillId,
        BigDecimal price,
        Duration duration,
        Integer levelId,
        Boolean isPublished
) {
}

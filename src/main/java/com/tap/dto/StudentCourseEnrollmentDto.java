package com.tap.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record StudentCourseEnrollmentDto(
        Integer enrollmentId,
        UUID studentId,
        String studentName,
        UUID courseId,
        String courseTitle,
        String status,
        BigDecimal progress,
        LocalDateTime enrolledAt
) {
}

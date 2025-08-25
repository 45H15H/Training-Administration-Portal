package com.tap.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record StudentBookingDto(
        Integer bookingId,
        UUID studentId,
        String studentName,
        UUID instructorId,
        String instructorName,
        Integer timeSlotId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String status,
        LocalDateTime bookedAt
) {
}

package com.tap.dto;

import java.util.UUID;

public record BookingRequestDto(
        UUID instructorId,
        Integer timeSlotId,
        UUID courseId
) {}

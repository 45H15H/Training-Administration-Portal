package com.tap.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class InstructorTimeSlotDto {
    private Integer slotId;
    private UUID instructorId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isBooked;
}
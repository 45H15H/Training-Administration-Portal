package com.tap.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class StudentPreferenceDto {
    private Integer preferenceId;
    private UUID studentId;
    private List<String> requiredSkills;
    private List<String> preferredTimeSlots;
    private LocalDateTime preferredStartTime;
    private LocalDateTime preferredEndTime;
}

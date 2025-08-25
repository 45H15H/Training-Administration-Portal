package com.tap.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class InstructorEarningDto {

    private Integer earningId;
    private UUID instructorId;
    private UUID studentId;
    private Integer bankDetailId;
    private BigDecimal amount;
    private String paymentMethod;
    private String notes;
    private LocalDateTime receivedAt;
}
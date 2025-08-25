package com.tap.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class InstructorEarningCreationDto {

    private UUID instructorId;
    private UUID studentId;
    private Integer bankDetailId;
    private BigDecimal amount;
    private String paymentMethod;
    private String notes;

}
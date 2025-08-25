package com.tap.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class StudentPaymentDto {
    private Integer paymentId;
    private UUID student;
    private Integer studentBankDetails;
    // Course for which this payment is made
    private UUID courseId;
    private BigDecimal amount;
    private String paymentMethod;
    private String paymentStatus;
    private LocalDateTime paidAt;
    private String notes;
}

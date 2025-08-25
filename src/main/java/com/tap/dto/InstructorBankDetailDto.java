package com.tap.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class InstructorBankDetailDto {
    private Integer bankDetailId;
    private UUID instructorId;
    private String accountHolderName;
    private String bankName;
    private String accountNumber;
    private String ifscCode;
    private String accountType;
    private String branchName;
    private LocalDateTime createdAt;
}
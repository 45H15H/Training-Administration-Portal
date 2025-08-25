package com.tap.dto;

import lombok.Data;

@Data
public class InstructorBankDetailCreationDto {
    private String accountHolderName;
    private String bankName;
    private String accountNumber;
    private String ifscCode;
    private String accountType;
    private String branchName;
}
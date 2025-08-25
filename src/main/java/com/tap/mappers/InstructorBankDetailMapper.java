package com.tap.mappers;

import com.tap.dto.InstructorBankDetailCreationDto;
import com.tap.dto.InstructorBankDetailDto;
import com.tap.entities.Instructor;
import com.tap.entities.InstructorBankDetail;
import org.springframework.stereotype.Component;

@Component
public class InstructorBankDetailMapper {

    public InstructorBankDetail toEntity(InstructorBankDetailCreationDto dto, Instructor instructor) {
        InstructorBankDetail detail = new InstructorBankDetail();
        detail.setInstructor(instructor);
        detail.setAccountHolderName(dto.getAccountHolderName());
        detail.setBankName(dto.getBankName());
        detail.setAccountNumber(dto.getAccountNumber());
        detail.setIfscCode(dto.getIfscCode());
        detail.setAccounttype(dto.getAccountType());
        detail.setBranchName(dto.getBranchName());
        return detail;
    }

    public InstructorBankDetailDto toDto(InstructorBankDetail entity) {
        InstructorBankDetailDto dto = new InstructorBankDetailDto();
        dto.setBankDetailId(entity.getBankDetailId());
        dto.setInstructorId(entity.getInstructor().getInstructorId());
        dto.setAccountHolderName(entity.getAccountHolderName());
        dto.setBankName(entity.getBankName());
        dto.setAccountNumber(entity.getAccountNumber());
        dto.setIfscCode(entity.getIfscCode());
        dto.setAccountType(entity.getAccounttype());
        dto.setBranchName(entity.getBranchName());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}
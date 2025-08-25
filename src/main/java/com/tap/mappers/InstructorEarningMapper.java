package com.tap.mappers;

import com.tap.dto.InstructorEarningCreationDto;
import com.tap.dto.InstructorEarningDto;
import com.tap.entities.InstructorEarning;
import com.tap.entities.Instructor;
import com.tap.entities.Student;
import com.tap.entities.InstructorBankDetail;
import org.springframework.stereotype.Component;

@Component
public class InstructorEarningMapper {

    public InstructorEarning toEntity(InstructorEarningCreationDto dto, Instructor instructor, Student student, InstructorBankDetail bankDetail) {
        InstructorEarning earning = new InstructorEarning();
        earning.setInstructor(instructor);

        // Only set student if not null
        if (student != null) {
            earning.setStudent(student);
        }

        // Only set bank detail if not null
        if (bankDetail != null) {
            earning.setBankDetail(bankDetail);
        }

        earning.setAmount(dto.getAmount());
        earning.setPaymentMethod(dto.getPaymentMethod());
        earning.setNotes(dto.getNotes());

        // receivedAt is auto-set in entity constructor or via @PrePersist
        return earning;
    }

    public InstructorEarningDto toDto(InstructorEarning entity) {
        InstructorEarningDto dto = new InstructorEarningDto();
        dto.setEarningId(entity.getEarningId());
        dto.setInstructorId(entity.getInstructor().getInstructorId());

        // Safely extract studentId and bankDetailId
        dto.setStudentId(entity.getStudent() != null ? entity.getStudent().getStudentId() : null);
        dto.setBankDetailId(entity.getBankDetail() != null ? entity.getBankDetail().getBankDetailId() : null);

        dto.setAmount(entity.getAmount());
        dto.setPaymentMethod(entity.getPaymentMethod());
        dto.setNotes(entity.getNotes());
        dto.setReceivedAt(entity.getReceivedAt());

        return dto;
    }
}
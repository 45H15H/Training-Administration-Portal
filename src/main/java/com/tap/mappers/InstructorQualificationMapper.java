package com.tap.mappers;

import com.tap.dto.InstructorQualificationDto;
import com.tap.entities.InstructorQualification;
import com.tap.entities.Instructor;
import org.springframework.stereotype.Component;

@Component
public class InstructorQualificationMapper {

    public InstructorQualification toEntity(InstructorQualificationDto dto, Instructor instructor) {
        InstructorQualification qualification = new InstructorQualification();
        qualification.setInstructor(instructor);
        qualification.setBio(dto.getBio());
        qualification.setHighestQualification(dto.getHighestQualification());
        qualification.setRelevantExperience(dto.getRelevantExperience());
        return qualification;
    }

    public InstructorQualificationDto toDto(InstructorQualification entity) {
        InstructorQualificationDto dto = new InstructorQualificationDto();
        dto.setInstructorId(entity.getInstructor().getInstructorId());
        dto.setBio(entity.getBio());
        dto.setHighestQualification(entity.getHighestQualification());
        dto.setRelevantExperience(entity.getRelevantExperience());
        return dto;
    }
}
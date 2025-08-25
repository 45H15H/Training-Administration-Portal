package com.tap.services;

import com.tap.dto.InstructorQualificationDto;
import com.tap.entities.Instructor;
import com.tap.entities.InstructorQualification;
import com.tap.exceptions.ResourceNotFoundException;
import com.tap.mappers.InstructorQualificationMapper;
import com.tap.repositories.InstructorQualificationRepository;
import com.tap.repositories.InstructorRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InstructorQualificationService {

    private final InstructorQualificationRepository qualificationRepository;
    private final InstructorRepository instructorRepository;
    private final InstructorQualificationMapper mapper;

    public InstructorQualificationService(InstructorQualificationRepository qualificationRepository,
                                          InstructorRepository instructorRepository,
                                          InstructorQualificationMapper mapper) {
        this.qualificationRepository = qualificationRepository;
        this.instructorRepository = instructorRepository;
        this.mapper = mapper;
    }

        public InstructorQualificationDto createOrUpdate(UUID instructorId, InstructorQualificationDto dto) {
                Instructor instructor = instructorRepository.findById(instructorId)
                                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));

        InstructorQualification qualification = qualificationRepository.findByInstructor(instructor)
                .orElse(mapper.toEntity(dto, instructor));

        qualification.setBio(dto.getBio());
        qualification.setHighestQualification(dto.getHighestQualification());
        qualification.setRelevantExperience(dto.getRelevantExperience());

        InstructorQualification saved = qualificationRepository.save(qualification);
        return mapper.toDto(saved);
    }

        public InstructorQualificationDto getByInstructor(UUID instructorId) {
                Instructor instructor = instructorRepository.findById(instructorId)
                                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));

        InstructorQualification qualification = qualificationRepository.findByInstructor(instructor)
                .orElseThrow(() -> new RuntimeException("Qualification not found"));

        return mapper.toDto(qualification);
    }

    public void deleteByInstructor(UUID instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));

        qualificationRepository.deleteByInstructor(instructor);
    }

    public InstructorQualificationDto updateQualification(UUID instructorId, InstructorQualificationDto dto) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
        InstructorQualification qualification = qualificationRepository.findByInstructor(instructor)
                .orElseThrow(() -> new ResourceNotFoundException("Qualification not found for instructor"));
        qualification.setBio(dto.getBio());
        qualification.setHighestQualification(dto.getHighestQualification());
        qualification.setRelevantExperience(dto.getRelevantExperience());
        InstructorQualification saved = qualificationRepository.save(qualification);
        return mapper.toDto(saved);
    }
}
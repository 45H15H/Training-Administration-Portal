package com.tap.services;

import com.tap.dto.InstructorSkillDto;
import com.tap.entities.Instructor;
import com.tap.entities.InstructorSkill;
import com.tap.entities.ProficiencyLevel;
import com.tap.mappers.InstructorSkillMapper;
import com.tap.repositories.InstructorRepository;
import com.tap.repositories.InstructorSkillRepository;
import com.tap.repositories.ProficiencyLevelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InstructorSkillService {

    private final InstructorSkillRepository skillRepository;
    private final InstructorRepository instructorRepository;
    private final ProficiencyLevelRepository levelRepository;
    private final InstructorSkillMapper mapper;

    public InstructorSkillService(InstructorSkillRepository skillRepository,
                                  InstructorRepository instructorRepository,
                                  ProficiencyLevelRepository levelRepository,
                                  InstructorSkillMapper mapper) {
        this.skillRepository = skillRepository;
        this.instructorRepository = instructorRepository;
        this.levelRepository = levelRepository;
        this.mapper = mapper;
    }

    public InstructorSkillDto addSkill(UUID instructorId, InstructorSkillDto dto) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        ProficiencyLevel level = levelRepository.findById(dto.getLevelId())
                .orElseThrow(() -> new RuntimeException("Proficiency level not found"));

        InstructorSkill skill = mapper.toEntity(dto, instructor, level);
        InstructorSkill saved = skillRepository.save(skill);
        return mapper.toDto(saved);
    }

    public List<InstructorSkillDto> getSkillsByInstructor(UUID instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        return skillRepository.findByInstructor(instructor).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteSkill(UUID instructorId, Integer skillId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        InstructorSkill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        if (!skill.getInstructor().getInstructorId().equals(instructor.getInstructorId())) {
            throw new RuntimeException("Skill does not belong to the instructor");
        }

        skillRepository.delete(skill);
    }
}
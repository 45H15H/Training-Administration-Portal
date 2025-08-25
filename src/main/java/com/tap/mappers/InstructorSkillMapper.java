package com.tap.mappers;

import com.tap.dto.InstructorSkillDto;
import com.tap.entities.Instructor;
import com.tap.entities.InstructorSkill;
import com.tap.entities.ProficiencyLevel;
import org.springframework.stereotype.Component;

@Component
public class InstructorSkillMapper {

    public InstructorSkill toEntity(InstructorSkillDto dto, Instructor instructor, ProficiencyLevel level) {
        InstructorSkill skill = new InstructorSkill();
        skill.setInstructor(instructor);
        skill.setLevel(level);
        skill.setSkillName(dto.getSkillName());
        return skill;
    }

    public InstructorSkillDto toDto(InstructorSkill entity) {
        InstructorSkillDto dto = new InstructorSkillDto();
        dto.setInstructorId(entity.getInstructor().getInstructorId());
        dto.setLevelId(entity.getLevel().getLevelId());
        dto.setSkillName(entity.getSkillName());
        return dto;
    }
}
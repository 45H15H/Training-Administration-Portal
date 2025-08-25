package com.tap.repositories;

import com.tap.entities.Instructor;
import com.tap.entities.InstructorSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstructorSkillRepository extends JpaRepository<InstructorSkill, Integer> {
    List<InstructorSkill> findByInstructor(Instructor instructor);
    void deleteByInstructor(Instructor instructor);
}
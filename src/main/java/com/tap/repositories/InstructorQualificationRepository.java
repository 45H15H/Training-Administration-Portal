package com.tap.repositories;

import com.tap.entities.Instructor;
import com.tap.entities.InstructorQualification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstructorQualificationRepository extends JpaRepository<InstructorQualification, Integer> {
    Optional<InstructorQualification> findByInstructor(Instructor instructor);
    void deleteByInstructor(Instructor instructor);
}
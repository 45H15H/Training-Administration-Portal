package com.tap.repositories;

import com.tap.entities.Instructor;
import com.tap.entities.InstructorResume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstructorResumeRepository extends JpaRepository<InstructorResume, Integer> {
    Optional<InstructorResume> findByInstructor(Instructor instructor);
}

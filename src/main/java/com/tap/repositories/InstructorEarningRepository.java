package com.tap.repositories;

import com.tap.entities.Instructor;
import com.tap.entities.InstructorEarning;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstructorEarningRepository extends JpaRepository<InstructorEarning, Integer> {
    List<InstructorEarning> findByInstructor(Instructor instructor);
}
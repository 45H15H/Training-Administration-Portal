package com.tap.repositories;

import com.tap.entities.Instructor;
import com.tap.entities.InstructorTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstructorTimeSlotRepository extends JpaRepository<InstructorTimeSlot, Integer> {
    List<InstructorTimeSlot> findByInstructor(Instructor instructor);
}
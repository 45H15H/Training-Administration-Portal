package com.tap.repositories;

import com.tap.entities.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, UUID> {
    Optional<Instructor> findById(UUID instructorId);

    Optional<Object> findByUserId(UUID instructorId);
}

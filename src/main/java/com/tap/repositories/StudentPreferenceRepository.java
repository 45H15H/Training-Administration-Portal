package com.tap.repositories;

import com.tap.entities.Student;
import com.tap.entities.StudentPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentPreferenceRepository extends JpaRepository<StudentPreference, Integer> {
    Optional<StudentPreference> findByStudent(Student student);
}

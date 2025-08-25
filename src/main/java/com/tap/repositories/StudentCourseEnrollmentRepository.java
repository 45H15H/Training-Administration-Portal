package com.tap.repositories;

import com.tap.entities.StudentCourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentCourseEnrollmentRepository extends JpaRepository<StudentCourseEnrollment, Long> {
    List<StudentCourseEnrollment> findByStudent_UserId(UUID studentId);
    boolean existsByStudent_UserIdAndCourse_CourseId(UUID studentId, UUID courseId);
}

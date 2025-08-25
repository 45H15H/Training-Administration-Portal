package com.tap.services;

import com.tap.dto.StudentCourseEnrollmentDto;
import com.tap.entities.Course;
import com.tap.entities.Student;
import com.tap.entities.StudentCourseEnrollment;
import com.tap.exceptions.ResourceNotFoundException;
import com.tap.mappers.UserMapper;
import com.tap.repositories.CourseRepository;
import com.tap.repositories.StudentCourseEnrollmentRepository;
import com.tap.repositories.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentCourseEnrollmentService {

    private final StudentCourseEnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final UserMapper userMapper;

    public StudentCourseEnrollmentService(StudentCourseEnrollmentRepository enrollmentRepository,
                                          StudentRepository studentRepository,
                                          CourseRepository courseRepository,
                                          UserMapper userMapper) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.userMapper = userMapper;
    }

    public List<StudentCourseEnrollmentDto> getEnrollmentsByStudentId(UUID studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found with id: " + studentId);
        }
        List<StudentCourseEnrollment> enrollments = enrollmentRepository.findByStudent_UserId(studentId);
        return enrollments.stream()
                .map(userMapper::toStudentCourseEnrollmentDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public StudentCourseEnrollmentDto updateEnrollmentProgress(Long enrollmentId, BigDecimal newProgress) {
        StudentCourseEnrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + enrollmentId));

        enrollment.setProgress(newProgress);

        if (newProgress.compareTo(new BigDecimal("100.0")) >= 0) {
            enrollment.setStatus("COMPLETED");
        }

        StudentCourseEnrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        return userMapper.toStudentCourseEnrollmentDto(updatedEnrollment);
    }
}

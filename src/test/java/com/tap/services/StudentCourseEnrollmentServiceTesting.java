package com.tap.services;

import com.tap.dto.StudentCourseEnrollmentDto;
import com.tap.entities.StudentCourseEnrollment;
import com.tap.exceptions.ResourceNotFoundException;
import com.tap.mappers.UserMapper;
import com.tap.repositories.CourseRepository;
import com.tap.repositories.StudentCourseEnrollmentRepository;
import com.tap.repositories.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentCourseEnrollmentServiceTesting {


    @Mock
    private StudentCourseEnrollmentRepository enrollmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private StudentCourseEnrollmentService enrollmentService;

    @Test
    void testGetEnrollmentsByStudentId_Success() {
        UUID studentId = UUID.randomUUID();
        StudentCourseEnrollment enrollment = new StudentCourseEnrollment();

        StudentCourseEnrollmentDto dto = new StudentCourseEnrollmentDto(
                1,
                studentId,
                "Barsha Parida",
                UUID.randomUUID(),
                "Java Basics",
                "IN_PROGRESS",
                new BigDecimal("50.0"),
                LocalDateTime.now()
        );

        when(studentRepository.existsById(studentId)).thenReturn(true);
        when(enrollmentRepository.findByStudent_UserId(studentId)).thenReturn(List.of(enrollment));
        when(userMapper.toStudentCourseEnrollmentDto(enrollment)).thenReturn(dto);

        List<StudentCourseEnrollmentDto> result = enrollmentService.getEnrollmentsByStudentId(studentId);

        assertEquals(1, result.size());
        verify(studentRepository).existsById(studentId);
        verify(enrollmentRepository).findByStudent_UserId(studentId);
        verify(userMapper).toStudentCourseEnrollmentDto(enrollment);
    }

    @Test
    void testGetEnrollmentsByStudentId_StudentNotFound() {
        UUID studentId = UUID.randomUUID();

        when(studentRepository.existsById(studentId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () ->
                enrollmentService.getEnrollmentsByStudentId(studentId));
    }

    @Test
    void testUpdateEnrollmentProgress_Success_NotCompleted() {
        Long enrollmentId = 1L;
        BigDecimal newProgress = new BigDecimal("50.0");
        StudentCourseEnrollment enrollment = new StudentCourseEnrollment();

        StudentCourseEnrollmentDto dto = new StudentCourseEnrollmentDto(
                enrollmentId.intValue(),
                UUID.randomUUID(),
                "Barsha Parida",
                UUID.randomUUID(),
                "Java Basics",
                null,
                newProgress,
                LocalDateTime.now()
        );

        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);
        when(userMapper.toStudentCourseEnrollmentDto(enrollment)).thenReturn(dto);

        StudentCourseEnrollmentDto result = enrollmentService.updateEnrollmentProgress(enrollmentId, newProgress);

        assertNotNull(result);
        assertEquals(newProgress, enrollment.getProgress());
        assertNull(enrollment.getStatus());
        verify(enrollmentRepository).save(enrollment);
    }

    @Test
    void testUpdateEnrollmentProgress_Success_Completed() {
        Long enrollmentId = 1L;
        BigDecimal newProgress = new BigDecimal("100.0");
        StudentCourseEnrollment enrollment = new StudentCourseEnrollment();

        StudentCourseEnrollmentDto dto = new StudentCourseEnrollmentDto(
                enrollmentId.intValue(),
                UUID.randomUUID(),
                "Barsha Parida",
                UUID.randomUUID(),
                "Java Basics",
                "COMPLETED",
                newProgress,
                LocalDateTime.now()
        );

        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);
        when(userMapper.toStudentCourseEnrollmentDto(enrollment)).thenReturn(dto);

        StudentCourseEnrollmentDto result = enrollmentService.updateEnrollmentProgress(enrollmentId, newProgress);

        assertNotNull(result);
        assertEquals("COMPLETED", enrollment.getStatus());
        verify(enrollmentRepository).save(enrollment);
    }

    @Test
    void testUpdateEnrollmentProgress_EnrollmentNotFound() {
        Long enrollmentId = 1L;
        BigDecimal newProgress = new BigDecimal("75.0");

        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                enrollmentService.updateEnrollmentProgress(enrollmentId, newProgress));
    }
}




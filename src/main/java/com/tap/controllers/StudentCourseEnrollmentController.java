package com.tap.controllers;

import com.tap.dto.ProgressUpdateDto;
import com.tap.dto.StudentCourseEnrollmentDto;
import com.tap.services.StudentCourseEnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/enrollments")
public class StudentCourseEnrollmentController {

    private final StudentCourseEnrollmentService enrollmentService;

    public StudentCourseEnrollmentController(StudentCourseEnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/me")
    public ResponseEntity<List<StudentCourseEnrollmentDto>> getMyEnrollments(Authentication authentication) {
        com.tap.services.CustomUserDetails userDetails = (com.tap.services.CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isStudent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UUID studentId = userDetails.getUser().getUserId();
        List<StudentCourseEnrollmentDto> enrollments = enrollmentService.getEnrollmentsByStudentId(studentId);
        return ResponseEntity.ok(enrollments);
    }

    @PutMapping("/{enrollmentId}/progress")
    public ResponseEntity<StudentCourseEnrollmentDto> updateProgress(
            @PathVariable Long enrollmentId,
            @RequestBody ProgressUpdateDto progressUpdateDto,
            Authentication authentication) {
        com.tap.services.CustomUserDetails userDetails = (com.tap.services.CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isStudent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        StudentCourseEnrollmentDto updatedEnrollment = enrollmentService.updateEnrollmentProgress(enrollmentId, progressUpdateDto.progress());
        return ResponseEntity.ok(updatedEnrollment);
    }
}

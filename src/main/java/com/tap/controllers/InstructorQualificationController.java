package com.tap.controllers;

import com.tap.dto.InstructorQualificationDto;
import com.tap.services.InstructorQualificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import com.tap.exceptions.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/instructors/me/qualification")
public class InstructorQualificationController {

    private final InstructorQualificationService service;

    public InstructorQualificationController(InstructorQualificationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<InstructorQualificationDto> createOrUpdate(Authentication authentication,
                                                                     @RequestBody InstructorQualificationDto dto) {
        com.tap.services.CustomUserDetails userDetails = (com.tap.services.CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UUID instructorId = userDetails.getUser().getUserId();
        return ResponseEntity.ok(service.createOrUpdate(instructorId, dto));
    }

    @PutMapping
    public ResponseEntity<?> update(Authentication authentication,
                                    @RequestBody InstructorQualificationDto dto) {
        com.tap.services.CustomUserDetails userDetails = (com.tap.services.CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UUID instructorId = userDetails.getUser().getUserId();
        try {
            return ResponseEntity.ok(service.updateQualification(instructorId, dto));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<InstructorQualificationDto> getQualification(Authentication authentication) {
        com.tap.services.CustomUserDetails userDetails = (com.tap.services.CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UUID instructorId = userDetails.getUser().getUserId();
        return ResponseEntity.ok(service.getByInstructor(instructorId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteQualification(Authentication authentication) {
        com.tap.services.CustomUserDetails userDetails = (com.tap.services.CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        UUID instructorId = userDetails.getUser().getUserId();
        service.deleteByInstructor(instructorId);
        return ResponseEntity.noContent().build();
    }
}
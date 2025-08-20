package com.tap.controllers;

import com.tap.dto.InstructorCreationDto;
import com.tap.dto.InstructorDto;
import com.tap.dto.InstructorResumeDto;
import com.tap.services.InstructorService;
import com.tap.exceptions.DuplicateResourceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
    @RequestMapping("/api/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @PostMapping
    public ResponseEntity<?> createInstructor(@RequestBody InstructorCreationDto instructorDto) {
        try {
            return new ResponseEntity<>(instructorService.createInstructor(instructorDto), HttpStatus.CREATED);
        } catch (DuplicateResourceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstructorDto> getInstructorById(@PathVariable UUID id) {
        return ResponseEntity.ok(instructorService.getInstructorById(id));
    }

    @GetMapping
    public ResponseEntity<List<InstructorDto>> getAllInstructors() {
        return ResponseEntity.ok(instructorService.getAllInstructors());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInstructor(@PathVariable UUID id, @RequestBody InstructorCreationDto instructorDto) {
        try {
            return ResponseEntity.ok(instructorService.updateInstructor(id, instructorDto));
        } catch (DuplicateResourceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/resume")
    public ResponseEntity<?> uploadResume(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        try {
            InstructorResumeDto resumeDto = instructorService.uploadResume(id, file);
            return new ResponseEntity<>(resumeDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/resume")
    public ResponseEntity<InstructorResumeDto> getResume(@PathVariable UUID id) {
        return ResponseEntity.ok(instructorService.getResumeByInstructorId(id));
    }

    @PutMapping("/{id}/resume")
    public ResponseEntity<?> updateResume(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        try {
            InstructorResumeDto resumeDto = instructorService.uploadResume(id, file);
            return ResponseEntity.ok(resumeDto);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

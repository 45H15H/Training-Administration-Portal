package com.tap.controllers;

import com.tap.dto.InstructorEarningCreationDto;
import com.tap.dto.InstructorEarningDto;
import com.tap.services.InstructorEarningService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/instructors/me/earnings")
public class InstructorEarningController {

    private final InstructorEarningService service;

    public InstructorEarningController(InstructorEarningService service) {
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<InstructorEarningDto> createEarning(Authentication authentication,
                                                              @RequestBody InstructorEarningCreationDto dto) {
        com.tap.services.CustomUserDetails userDetails = (com.tap.services.CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(403).build();
        }
        UUID instructorId = userDetails.getUser().getUserId();
        return ResponseEntity.ok(service.createEarning(instructorId, dto));
    }

    @GetMapping
    public ResponseEntity<List<InstructorEarningDto>> getEarningsByInstructor(Authentication authentication) {
        com.tap.services.CustomUserDetails userDetails = (com.tap.services.CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(403).build();
        }
        UUID instructorId = userDetails.getUser().getUserId();
        return ResponseEntity.ok(service.getEarningsByInstructor(instructorId));
    }

//    @GetMapping
//    public ResponseEntity<List<InstructorEarningDto>> list(@PathVariable UUID instructorId) {
//        return ResponseEntity.ok(service.getEarningsByInstructor(instructorId));
//    }

    @PutMapping
    public ResponseEntity<InstructorEarningDto> updateEarning(Authentication authentication,
                                                              @RequestBody InstructorEarningCreationDto dto) {
        com.tap.services.CustomUserDetails userDetails = (com.tap.services.CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(403).build();
        }
        UUID instructorId = userDetails.getUser().getUserId();
        return ResponseEntity.ok(service.updateEarningByInstructor(instructorId, dto));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteEarning(Authentication authentication,
                                              @RequestBody InstructorEarningCreationDto dto) {
        com.tap.services.CustomUserDetails userDetails = (com.tap.services.CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(403).build();
        }
        UUID instructorId = userDetails.getUser().getUserId();
        service.deleteEarningByInstructor(instructorId, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{earningId}")
    public ResponseEntity<Void> delete(Authentication authentication, @PathVariable Integer earningId) {
        com.tap.services.CustomUserDetails userDetails = (com.tap.services.CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(403).build();
        }
        service.deleteEarning(earningId); // instructorId not needed
        return ResponseEntity.noContent().build();
    }
}
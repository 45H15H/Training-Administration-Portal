package com.tap.controllers;

import com.tap.dto.InstructorTimeSlotDto;
import com.tap.services.InstructorTimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/instructors/me/slots")
public class InstructorTimeSlotController {

    @Autowired
    private InstructorTimeSlotService service;

    @PostMapping
    public ResponseEntity<InstructorTimeSlotDto> createSlot(
            Authentication authentication,
            @RequestBody InstructorTimeSlotDto dto) {
        com.tap.services.CustomUserDetails userDetails = (com.tap.services.CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(403).build();
        }
        UUID instructorId = userDetails.getUser().getUserId();
        return ResponseEntity.status(201).body(service.createSlot(instructorId, dto));
    }

    @GetMapping
    public ResponseEntity<List<InstructorTimeSlotDto>> getSlots(Authentication authentication) {
        com.tap.services.CustomUserDetails userDetails = (com.tap.services.CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(403).build();
        }
        UUID instructorId = userDetails.getUser().getUserId();
        return ResponseEntity.ok(service.getSlots(instructorId));
    }

    @PutMapping("/{slotId}")
    public ResponseEntity<InstructorTimeSlotDto> updateSlot(
            Authentication authentication,
            @PathVariable Integer slotId,
            @RequestBody InstructorTimeSlotDto dto) {
        com.tap.services.CustomUserDetails userDetails = (com.tap.services.CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(403).build();
        }
        UUID instructorId = userDetails.getUser().getUserId();
        return ResponseEntity.ok(service.updateSlot(instructorId, slotId, dto));
    }

    @DeleteMapping("/{slotId}")
    public ResponseEntity<Void> deleteSlot(@PathVariable Integer slotId) {
        service.deleteSlot(slotId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllSlots(Authentication authentication) {
        com.tap.services.CustomUserDetails userDetails = (com.tap.services.CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(403).build();
        }
        UUID instructorId = userDetails.getUser().getUserId();
        service.deleteAllSlots(instructorId);
        return ResponseEntity.ok("All slots deleted for instructor " + instructorId);
    }
}
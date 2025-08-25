package com.tap.controllers;

import com.tap.dto.InstructorSkillDto;
import com.tap.services.InstructorSkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/instructors/me/skills")
public class InstructorSkillController {

    private final InstructorSkillService service;

    public InstructorSkillController(InstructorSkillService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<InstructorSkillDto> addSkill(Authentication authentication,
                                                       @RequestBody InstructorSkillDto dto) {
        com.tap.services.CustomUserDetails userDetails = (com.tap.services.CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(403).build();
        }
        UUID instructorId = userDetails.getUser().getUserId();
        return ResponseEntity.ok(service.addSkill(instructorId, dto));
    }

    @GetMapping
    public ResponseEntity<List<InstructorSkillDto>> getSkills(Authentication authentication) {
        com.tap.services.CustomUserDetails userDetails = (com.tap.services.CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(403).build();
        }
        UUID instructorId = userDetails.getUser().getUserId();
        return ResponseEntity.ok(service.getSkillsByInstructor(instructorId));
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<Void> deleteSkill(Authentication authentication,
                                            @PathVariable Integer skillId) {
        com.tap.services.CustomUserDetails userDetails = (com.tap.services.CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(403).build();
        }
        UUID instructorId = userDetails.getUser().getUserId();
        service.deleteSkill(instructorId, skillId);
        return ResponseEntity.noContent().build();
    }
}
package com.tap.controllers;

import com.tap.dto.InstructorBankDetailCreationDto;
import com.tap.dto.InstructorBankDetailDto;
import com.tap.services.InstructorBankDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/instructors/me/bank-details")
public class InstructorBankDetailsController {

    private final InstructorBankDetailsService service;

    public InstructorBankDetailsController(InstructorBankDetailsService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<InstructorBankDetailDto>> getBankDetails(Authentication authentication) {
        com.tap.services.CustomUserDetails userDetails = (com.tap.services.CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(403).build();
        }
        UUID instructorId = userDetails.getUser().getUserId();
        return ResponseEntity.ok(service.getBankDetailsByInstructorId(instructorId));
    }

    @PostMapping
    public ResponseEntity<InstructorBankDetailDto> addBankDetail(Authentication authentication,
                                                                 @RequestBody InstructorBankDetailCreationDto dto) {
        com.tap.services.CustomUserDetails userDetails = (com.tap.services.CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(403).build();
        }
        UUID instructorId = userDetails.getUser().getUserId();
        return ResponseEntity.ok(service.addBankDetail(instructorId, dto));
    }

    @PutMapping("/{bankDetailId}")
    public ResponseEntity<InstructorBankDetailDto> updateBankDetail(Authentication authentication,
                                                                    @PathVariable Integer bankDetailId,
                                                                    @RequestBody InstructorBankDetailCreationDto dto) {
        com.tap.services.CustomUserDetails userDetails = (com.tap.services.CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(service.updateBankDetail(bankDetailId, dto));
    }

    @DeleteMapping("/{bankDetailId}")
    public ResponseEntity<Void> deleteBankDetail(Authentication authentication,
                                                 @PathVariable Integer bankDetailId) {
        com.tap.services.CustomUserDetails userDetails = (com.tap.services.CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isInstructor()) {
            return ResponseEntity.status(403).build();
        }
        service.deleteBankDetail(bankDetailId);
        return ResponseEntity.noContent().build();
    }
}
package com.tap.controllers;

import com.tap.dto.BookingRequestDto;
import com.tap.dto.StudentBookingDto;
import com.tap.services.StudentBookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class StudentBookingController {

    private final StudentBookingService bookingService;

    public StudentBookingController(StudentBookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/me")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequestDto bookingRequest, Authentication authentication) {
        com.tap.services.CustomUserDetails userDetails = (com.tap.services.CustomUserDetails) authentication.getPrincipal();
        if (!userDetails.isStudent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        try {
            StudentBookingDto bookingDto = bookingService.createBooking(userDetails.getUser().getUserId(), bookingRequest);
            return new ResponseEntity<>(bookingDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

package com.tap.controller;

import com.tap.controllers.StudentBookingController;
import com.tap.dto.BookingRequestDto;
import com.tap.dto.StudentBookingDto;
import com.tap.entities.User;
import com.tap.services.CustomUserDetails;
import com.tap.services.StudentBookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentBookingControllerTest {

    @InjectMocks
    private StudentBookingController bookingController;

    @Mock
    private StudentBookingService bookingService;

    @Mock
    private Authentication authentication;

    @Mock
    private CustomUserDetails userDetails;

    private BookingRequestDto bookingRequest;
    private StudentBookingDto studentBookingDto;
    private UUID studentId;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();

        User user = new User();
        user.setUserId(studentId);

        bookingRequest = new BookingRequestDto(UUID.randomUUID(),101,UUID.randomUUID());
        studentBookingDto = new StudentBookingDto( 1, // bookingId
                studentId,
                "John Doe",
                UUID.randomUUID(),
                "Jane Smith",
                101,
                LocalDateTime.of(2025, 8, 23, 10, 0),
                LocalDateTime.of(2025, 8, 23, 11, 0),
                "CONFIRMED",
                LocalDateTime.now());
        //studentBookingDto.setStudentId(studentId);

//        bookingController = new StudentBookingController(bookingService);
//
//        when(authentication.getPrincipal()).thenReturn(userDetails);
//        when(userDetails.getUser()).thenReturn(user);
    }

    @Test
    void testCreateBooking_AsStudent_ReturnsCreated() {
        User user = new User();
        user.setUserId(studentId);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUser()).thenReturn(user);
        when(userDetails.isStudent()).thenReturn(true);
        when(bookingService.createBooking(studentId, bookingRequest)).thenReturn(studentBookingDto);

        ResponseEntity<?> response = bookingController.createBooking(bookingRequest, authentication);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(studentBookingDto, response.getBody());
    }

    @Test
    void testCreateBooking_NotStudent_ReturnsForbidden() {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.isStudent()).thenReturn(false);
        ResponseEntity<?> response = bookingController.createBooking(bookingRequest, authentication);

        assertEquals(403, response.getStatusCodeValue());
        assertEquals("Access denied", response.getBody());
    }

    @Test
    void testCreateBooking_ServiceThrowsException_ReturnsBadRequest() {
        User user = new User();
        user.setUserId(studentId);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUser()).thenReturn(user);
        when(userDetails.isStudent()).thenReturn(true);
        when(bookingService.createBooking(studentId, bookingRequest))
                .thenThrow(new RuntimeException("Booking failed"));

        ResponseEntity<?> response = bookingController.createBooking(bookingRequest, authentication);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Booking failed", response.getBody());
    }

}

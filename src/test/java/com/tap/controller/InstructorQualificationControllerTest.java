package com.tap.controller;

import com.tap.controllers.InstructorQualificationController;
import com.tap.dto.InstructorQualificationDto;
import com.tap.entities.User;
import com.tap.exceptions.ResourceNotFoundException;
import com.tap.services.CustomUserDetails;
import com.tap.services.InstructorQualificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class InstructorQualificationControllerTest {
    @InjectMocks
    private InstructorQualificationController controller;

    @Mock
    private InstructorQualificationService service;

    @Mock
    private Authentication authentication;

    @Mock
    private CustomUserDetails userDetails;

    private InstructorQualificationDto qualificationDto;
    private UUID instructorId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        instructorId = UUID.randomUUID();

        User user = new User();
        user.setUserId(instructorId);

        qualificationDto = new InstructorQualificationDto();
        qualificationDto.setInstructorId(instructorId);
        qualificationDto.setHighestQualification("PhD in Computer Science");

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUser()).thenReturn(user);
    }

    @Test
    void testCreateOrUpdate_AsInstructor_ReturnsOk() {
        when(userDetails.isInstructor()).thenReturn(true);
        when(service.createOrUpdate(instructorId, qualificationDto)).thenReturn(qualificationDto);

        ResponseEntity<InstructorQualificationDto> response = controller.createOrUpdate(authentication, qualificationDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(qualificationDto, response.getBody());
    }

    @Test
    void testCreateOrUpdate_NotInstructor_ReturnsForbidden() {
        when(userDetails.isInstructor()).thenReturn(false);

        ResponseEntity<InstructorQualificationDto> response = controller.createOrUpdate(authentication, qualificationDto);

        assertEquals(403, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testUpdate_AsInstructor_ReturnsOk() {
        when(userDetails.isInstructor()).thenReturn(true);
        when(service.updateQualification(instructorId, qualificationDto)).thenReturn(qualificationDto);

        ResponseEntity<?> response = controller.update(authentication, qualificationDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(qualificationDto, response.getBody());
    }

    @Test
    void testUpdate_ResourceNotFound_ReturnsNotFound() {
        when(userDetails.isInstructor()).thenReturn(true);
        when(service.updateQualification(instructorId, qualificationDto))
                .thenThrow(new ResourceNotFoundException("Qualification not found"));

        ResponseEntity<?> response = controller.update(authentication, qualificationDto);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Qualification not found", response.getBody());
    }

    @Test
    void testUpdate_NotInstructor_ReturnsForbidden() {
        when(userDetails.isInstructor()).thenReturn(false);

        ResponseEntity<?> response = controller.update(authentication, qualificationDto);

        assertEquals(403, response.getStatusCodeValue());
    }

    @Test
    void testGetQualification_AsInstructor_ReturnsOk() {
        when(userDetails.isInstructor()).thenReturn(true);
        when(service.getByInstructor(instructorId)).thenReturn(qualificationDto);

        ResponseEntity<InstructorQualificationDto> response = controller.getQualification(authentication);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(qualificationDto, response.getBody());
    }

    @Test
    void testGetQualification_NotInstructor_ReturnsForbidden() {
        when(userDetails.isInstructor()).thenReturn(false);

        ResponseEntity<InstructorQualificationDto> response = controller.getQualification(authentication);

        assertEquals(403, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteQualification_AsInstructor_ReturnsNoContent() {
        when(userDetails.isInstructor()).thenReturn(true);

        ResponseEntity<Void> response = controller.deleteQualification(authentication);

        verify(service).deleteByInstructor(instructorId);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testDeleteQualification_NotInstructor_ReturnsForbidden() {
        when(userDetails.isInstructor()).thenReturn(false);

        ResponseEntity<Void> response = controller.deleteQualification(authentication);

        verify(service, never()).deleteByInstructor(any());
        assertEquals(403, response.getStatusCodeValue());
    }

}

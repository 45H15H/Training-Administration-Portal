package com.tap.controller;

import com.tap.controllers.InstructorEarningController;
import com.tap.dto.InstructorEarningCreationDto;
import com.tap.dto.InstructorEarningDto;
import com.tap.entities.User;
import com.tap.services.CustomUserDetails;
import com.tap.services.InstructorEarningService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class InstructorEarningControllerTest {
    @Mock
    private InstructorEarningService service;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private InstructorEarningController controller;

    private UUID instructorId;
    private CustomUserDetails instructorUserDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        instructorId = UUID.randomUUID();

        User user = new User();
        user.setUserId(instructorId);

        instructorUserDetails = mock(CustomUserDetails.class);
        when(instructorUserDetails.getUser()).thenReturn(user);
        when(instructorUserDetails.isInstructor()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(instructorUserDetails);
    }

    @Test
    void testCreateEarning_AsInstructor() {
        InstructorEarningCreationDto creationDto = new InstructorEarningCreationDto();
        InstructorEarningDto responseDto = new InstructorEarningDto();
        responseDto.setInstructorId(instructorId);
        responseDto.setAmount(BigDecimal.valueOf(5000.0));
        responseDto.setReceivedAt(LocalDateTime.now());

        when(service.createEarning(instructorId, creationDto)).thenReturn(responseDto);

        ResponseEntity<InstructorEarningDto> response = controller.createEarning(authentication, creationDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(BigDecimal.valueOf(5000.0), response.getBody().getAmount());
    }

    @Test
    void testGetEarnings_AsInstructor() {
        InstructorEarningDto dto = new InstructorEarningDto();
        dto.setInstructorId(instructorId);
        dto.setAmount(BigDecimal.valueOf(5000.0));
        dto.setReceivedAt(LocalDateTime.now());

        when(service.getEarningsByInstructor(instructorId)).thenReturn(List.of(dto));

        ResponseEntity<List<InstructorEarningDto>> response = controller.getEarningsByInstructor(authentication);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(BigDecimal.valueOf(5000.0), response.getBody().get(0).getAmount());
    }


    @Test
    void testUpdateEarning_AsInstructor() {
        InstructorEarningCreationDto updateDto = new InstructorEarningCreationDto();
        InstructorEarningDto updated = new InstructorEarningDto();
        updated.setInstructorId(instructorId);
        updated.setAmount(BigDecimal.valueOf(7000.0));
        updated.setReceivedAt(LocalDateTime.now());

        when(service.updateEarningByInstructor(instructorId, updateDto)).thenReturn(updated);

        ResponseEntity<InstructorEarningDto> response = controller.updateEarning(authentication, updateDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(BigDecimal.valueOf(7000.0), response.getBody().getAmount());
    }


    @Test
    void testDeleteEarning_ByDto_AsInstructor() {
        InstructorEarningCreationDto dto = new InstructorEarningCreationDto();

        ResponseEntity<Void> response = controller.deleteEarning(authentication, dto);

        verify(service).deleteEarningByInstructor(instructorId, dto);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testDeleteEarning_ById_AsInstructor() {
        int earningId = 101;

        ResponseEntity<Void> response = controller.delete(authentication, earningId);

        verify(service).deleteEarning(earningId);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testUnauthorizedAccess() {
        when(instructorUserDetails.isInstructor()).thenReturn(false);

        ResponseEntity<InstructorEarningDto> createResponse = controller.createEarning(authentication, new InstructorEarningCreationDto());
        ResponseEntity<List<InstructorEarningDto>> getResponse = controller.getEarningsByInstructor(authentication);
        ResponseEntity<InstructorEarningDto> updateResponse = controller.updateEarning(authentication, new InstructorEarningCreationDto());
        ResponseEntity<Void> deleteDtoResponse = controller.deleteEarning(authentication, new InstructorEarningCreationDto());
        ResponseEntity<Void> deleteIdResponse = controller.delete(authentication, 1);

        assertEquals(403, createResponse.getStatusCodeValue());
        assertEquals(403, getResponse.getStatusCodeValue());
        assertEquals(403, updateResponse.getStatusCodeValue());
        assertEquals(403, deleteDtoResponse.getStatusCodeValue());
        assertEquals(403, deleteIdResponse.getStatusCodeValue());
    }
}


package com.tap.controller;

import com.tap.controllers.InstructorBankDetailsController;
import com.tap.dto.InstructorBankDetailCreationDto;
import com.tap.dto.InstructorBankDetailDto;
import com.tap.entities.User;
import com.tap.services.CustomUserDetails;
import com.tap.services.InstructorBankDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class InstructorBankDetailsControllerTest {
    @InjectMocks
    private InstructorBankDetailsController bankDetailsController;

    @Mock
    private InstructorBankDetailsService bankDetailsService;

    @Mock
    private Authentication authentication;

    @Mock
    private CustomUserDetails userDetails;

    private UUID instructorId;
    private InstructorBankDetailCreationDto bankDetailCreationDto;
    private InstructorBankDetailDto bankDetailDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        instructorId = UUID.randomUUID();

        User user = new User();
        user.setUserId(instructorId);

        bankDetailCreationDto = new InstructorBankDetailCreationDto();
        bankDetailDto = new InstructorBankDetailDto();
        bankDetailDto.setInstructorId(instructorId);
        bankDetailDto.setBankName("Test Bank");

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUser()).thenReturn(user);
    }

    @Test
    void testGetBankDetails_AsInstructor_ReturnsOk() {
        when(userDetails.isInstructor()).thenReturn(true);
        List<InstructorBankDetailDto> details = List.of(bankDetailDto);
        when(bankDetailsService.getBankDetailsByInstructorId(instructorId)).thenReturn(details);

        ResponseEntity<List<InstructorBankDetailDto>> response = bankDetailsController.getBankDetails(authentication);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(details, response.getBody());
    }

    @Test
    void testGetBankDetails_NotInstructor_ReturnsForbidden() {
        when(userDetails.isInstructor()).thenReturn(false);

        ResponseEntity<List<InstructorBankDetailDto>> response = bankDetailsController.getBankDetails(authentication);

        assertEquals(403, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testAddBankDetail_AsInstructor_ReturnsOk() {
        when(userDetails.isInstructor()).thenReturn(true);
        when(bankDetailsService.addBankDetail(instructorId, bankDetailCreationDto)).thenReturn(bankDetailDto);

        ResponseEntity<InstructorBankDetailDto> response = bankDetailsController.addBankDetail(authentication, bankDetailCreationDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(bankDetailDto, response.getBody());
    }

    @Test
    void testAddBankDetail_NotInstructor_ReturnsForbidden() {
        when(userDetails.isInstructor()).thenReturn(false);

        ResponseEntity<InstructorBankDetailDto> response = bankDetailsController.addBankDetail(authentication, bankDetailCreationDto);

        assertEquals(403, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateBankDetail_AsInstructor_ReturnsOk() {
        int bankDetailId = 1;
        when(userDetails.isInstructor()).thenReturn(true);
        when(bankDetailsService.updateBankDetail(bankDetailId, bankDetailCreationDto)).thenReturn(bankDetailDto);

        ResponseEntity<InstructorBankDetailDto> response = bankDetailsController.updateBankDetail(authentication, bankDetailId, bankDetailCreationDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(bankDetailDto, response.getBody());
    }

    @Test
    void testUpdateBankDetail_NotInstructor_ReturnsForbidden() {
        int bankDetailId = 1;
        when(userDetails.isInstructor()).thenReturn(false);

        ResponseEntity<InstructorBankDetailDto> response = bankDetailsController.updateBankDetail(authentication, bankDetailId, bankDetailCreationDto);

        assertEquals(403, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteBankDetail_AsInstructor_ReturnsNoContent() {
        int bankDetailId = 1;
        when(userDetails.isInstructor()).thenReturn(true);

        ResponseEntity<Void> response = bankDetailsController.deleteBankDetail(authentication, bankDetailId);

        verify(bankDetailsService).deleteBankDetail(bankDetailId);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testDeleteBankDetail_NotInstructor_ReturnsForbidden() {
        int bankDetailId = 1;
        when(userDetails.isInstructor()).thenReturn(false);

        ResponseEntity<Void> response = bankDetailsController.deleteBankDetail(authentication, bankDetailId);

        verify(bankDetailsService, never()).deleteBankDetail(anyInt());
        assertEquals(403, response.getStatusCodeValue());
    }
}

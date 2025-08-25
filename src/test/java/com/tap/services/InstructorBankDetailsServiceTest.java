package com.tap.services;

import com.tap.dto.InstructorBankDetailCreationDto;
import com.tap.dto.InstructorBankDetailDto;
import com.tap.entities.Instructor;
import com.tap.entities.InstructorBankDetail;
import com.tap.mappers.InstructorBankDetailMapper;
import com.tap.repositories.InstructorBankDetailRepository;
import com.tap.repositories.InstructorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InstructorBankDetailsServiceTest {

    //Here mock object is created for actual class . for this we used @mock to mock the object
    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private InstructorBankDetailMapper mapper;

    @Mock
    private InstructorBankDetailRepository instructorBankDetailRepository;

    @InjectMocks
    private InstructorBankDetailsService service;

    //we used void setup() beacuse it is called automatically before each test .so we don't have to repeat this setup every time.
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // activates the annotations before each test cases
    }

    @Test
    void testGetBankDetailsByInstructorId() {
        UUID instructorId = UUID.randomUUID();
        InstructorBankDetail detail = new InstructorBankDetail();
        InstructorBankDetailDto dto = new InstructorBankDetailDto();

        when(instructorBankDetailRepository.findByInstructorUserId(instructorId)).thenReturn(List.of(detail));
        when(mapper.toDto(detail)).thenReturn(dto);

        List<InstructorBankDetailDto> result = service.getBankDetailsByInstructorId(instructorId);

        assertEquals(1, result.size());
        verify(instructorBankDetailRepository).findByInstructorUserId(instructorId);
        verify(mapper).toDto(detail);
    }

    @Test
    void testAddBankDetail() {
        UUID instructorId = UUID.randomUUID();
        Instructor instructor = new Instructor();
        InstructorBankDetailCreationDto creationDto = new InstructorBankDetailCreationDto();
        InstructorBankDetail detail = new InstructorBankDetail();
        InstructorBankDetailDto dto = new InstructorBankDetailDto();


        when(instructorRepository.findByUserId(instructorId)).thenReturn(Optional.of(instructor));
        when(mapper.toEntity(creationDto, instructor)).thenReturn(detail);
        when(instructorBankDetailRepository.save(detail)).thenReturn(detail);
        when(mapper.toDto(detail)).thenReturn(dto);

        InstructorBankDetailDto result = service.addBankDetail(instructorId, creationDto);

        assertNotNull(result);
        verify(instructorRepository).findByUserId(instructorId);
        verify(mapper).toEntity(creationDto, instructor);
        verify(instructorBankDetailRepository).save(detail);
        verify(mapper).toDto(detail);
    }


    @Test
    void testAddBankDetail_InstructorNotFound() {
        UUID instructorId = UUID.randomUUID();
        InstructorBankDetailCreationDto creationDto = new InstructorBankDetailCreationDto();

        when(instructorRepository.findByUserId(instructorId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                service.addBankDetail(instructorId, creationDto));

        assertEquals("Instructor not found", exception.getMessage());
    }


    @Test
    void testUpdateBankDetail() {
        Integer bankDetailId = 1;
        InstructorBankDetailCreationDto creationDto = new InstructorBankDetailCreationDto();
        InstructorBankDetail existing = new InstructorBankDetail();
        InstructorBankDetailDto dto = new InstructorBankDetailDto();

        when(instructorBankDetailRepository.findById(bankDetailId)).thenReturn(Optional.of(existing));
        when(instructorBankDetailRepository.save(existing)).thenReturn(existing);
        when(mapper.toDto(existing)).thenReturn(dto);

        InstructorBankDetailDto result = service.updateBankDetail(bankDetailId, creationDto);

        assertNotNull(result);
        verify(instructorBankDetailRepository).findById(bankDetailId);
        verify(instructorBankDetailRepository).save(existing);
        verify(mapper).toDto(existing);
    }


    @Test
    void testUpdateBankDetail_NotFound() {
        Integer bankDetailId = 1;
        InstructorBankDetailCreationDto creationDto = new InstructorBankDetailCreationDto();

        when(instructorBankDetailRepository.findById(bankDetailId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                service.updateBankDetail(bankDetailId, creationDto));

        assertEquals("Bank detail not found", exception.getMessage());
    }


    @Test
    void testDeleteBankDetail() {
        Integer bankDetailId = 1;

        service.deleteBankDetail(bankDetailId);

        verify(instructorBankDetailRepository).deleteById(bankDetailId);
    }
}


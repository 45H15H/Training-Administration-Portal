package com.tap.services;

import com.tap.dto.InstructorEarningCreationDto;
import com.tap.dto.InstructorEarningDto;
import com.tap.entities.Instructor;
import com.tap.entities.InstructorBankDetail;
import com.tap.entities.InstructorEarning;
import com.tap.entities.Student;
import com.tap.mappers.InstructorEarningMapper;
import com.tap.repositories.InstructorBankDetailRepository;
import com.tap.repositories.InstructorEarningRepository;
import com.tap.repositories.InstructorRepository;
import com.tap.repositories.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InstructorEarningServiceTest {

    @Mock
    private InstructorEarningRepository earningRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private InstructorBankDetailRepository bankDetailRepository;


    @Mock
    private InstructorEarningMapper mapper;

    @InjectMocks
    private InstructorEarningService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateEarning() {
        UUID instructorId = UUID.randomUUID();
        Instructor instructor = new Instructor();
        Student student = new Student();
        InstructorBankDetail bankDetail = new InstructorBankDetail();
        InstructorEarningCreationDto dto = new InstructorEarningCreationDto();
        dto.setStudentId(UUID.randomUUID());
        dto.setBankDetailId(1);
        InstructorEarning earning = new InstructorEarning();
        InstructorEarningDto earningDto = new InstructorEarningDto();

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(studentRepository.findById(dto.getStudentId())).thenReturn(Optional.of(student));
        when(bankDetailRepository.findById(dto.getBankDetailId())).thenReturn(Optional.of(bankDetail));
        when(mapper.toEntity(dto, instructor, student, bankDetail)).thenReturn(earning);
        when(earningRepository.save(earning)).thenReturn(earning);
        when(mapper.toDto(earning)).thenReturn(earningDto);

        InstructorEarningDto result = service.createEarning(instructorId, dto);

        assertNotNull(result);
        verify(instructorRepository).findById(instructorId);
        verify(studentRepository).findById(dto.getStudentId());
        verify(bankDetailRepository).findById(dto.getBankDetailId());
        verify(earningRepository).save(earning);
        verify(mapper).toDto(earning);
    }

    @Test
    void testGetEarningsByInstructor() {
        UUID instructorId = UUID.randomUUID();
        Instructor instructor = new Instructor();
        InstructorEarning earning = new InstructorEarning();
        InstructorEarningDto dto = new InstructorEarningDto();

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(earningRepository.findByInstructor(instructor)).thenReturn(List.of(earning));
        when(mapper.toDto(earning)).thenReturn(dto);

        List<InstructorEarningDto> result = service.getEarningsByInstructor(instructorId);

        assertEquals(1, result.size());
        verify(instructorRepository).findById(instructorId);
        verify(earningRepository).findByInstructor(instructor);
        verify(mapper).toDto(earning);
    }

    @Test
    void testUpdateEarningByInstructor() {
        UUID instructorId = UUID.randomUUID();
        Instructor instructor = new Instructor();
        InstructorEarningCreationDto dto = new InstructorEarningCreationDto();
        dto.setAmount(BigDecimal.valueOf(1000.0));
        dto.setPaymentMethod("UPI");
        InstructorEarning earning = new InstructorEarning();
        earning.setAmount(BigDecimal.valueOf(1000.0));
        earning.setPaymentMethod("UPI");
        InstructorEarningDto earningDto = new InstructorEarningDto();

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(earningRepository.findByInstructor(instructor)).thenReturn(List.of(earning));
        when(earningRepository.save(earning)).thenReturn(earning);
        when(mapper.toDto(earning)).thenReturn(earningDto);

        InstructorEarningDto result = service.updateEarningByInstructor(instructorId, dto);

        assertNotNull(result);
        verify(instructorRepository).findById(instructorId);
        verify(earningRepository).findByInstructor(instructor);
        verify(earningRepository).save(earning);
        verify(mapper).toDto(earning);
    }

    @Test
    void testDeleteEarningByInstructor() {
        UUID instructorId = UUID.randomUUID();
        Instructor instructor = new Instructor();
        InstructorEarningCreationDto dto = new InstructorEarningCreationDto();
        dto.setAmount(BigDecimal.valueOf(1000.0));
        dto.setPaymentMethod("UPI");
        InstructorEarning earning = new InstructorEarning();
        earning.setAmount(BigDecimal.valueOf(1000.0));
        earning.setPaymentMethod("UPI");

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(earningRepository.findByInstructor(instructor)).thenReturn(List.of(earning));

        service.deleteEarningByInstructor(instructorId, dto);

        verify(earningRepository).delete(earning);
    }

    @Test
    void testGetEarningsForInstructor() {
        UUID instructorId = UUID.randomUUID();
        Instructor instructor = new Instructor();
        InstructorEarning earning = new InstructorEarning();
        InstructorEarningDto dto = new InstructorEarningDto();

        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        when(earningRepository.findByInstructor(instructor)).thenReturn(List.of(earning));
        when(mapper.toDto(earning)).thenReturn(dto);

        List<InstructorEarningDto> result = service.getEarningsForInstructor(instructorId);

        assertEquals(1, result.size());
        verify(instructorRepository).findById(instructorId);
        verify(earningRepository).findByInstructor(instructor);
        verify(mapper).toDto(earning);
    }

    @Test
    void testDeleteEarning() {
        Integer earningId = 1;

        when(earningRepository.existsById(earningId)).thenReturn(true);

        service.deleteEarning(earningId);

        verify(earningRepository).deleteById(earningId);
    }

    @Test
    void testDeleteEarning_NotFound() {
        Integer earningId = 1;

        when(earningRepository.existsById(earningId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                service.deleteEarning(earningId));

        assertEquals("Earning not found", exception.getMessage());
    }
}




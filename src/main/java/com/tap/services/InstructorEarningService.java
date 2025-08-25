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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InstructorEarningService {

    private final InstructorEarningRepository earningRepository;
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;
    private final InstructorBankDetailRepository bankDetailRepository;
    private final InstructorEarningMapper mapper;

    public InstructorEarningService(InstructorEarningRepository earningRepository,
                                    InstructorRepository instructorRepository,
                                    StudentRepository studentRepository,
                                    InstructorBankDetailRepository bankDetailRepository,
                                    InstructorEarningMapper mapper) {
        this.earningRepository = earningRepository;
        this.instructorRepository = instructorRepository;
        this.studentRepository = studentRepository;
        this.bankDetailRepository = bankDetailRepository;
        this.mapper = mapper;
    }

    public InstructorEarningDto createEarning(UUID instructorId, InstructorEarningCreationDto dto) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        Student student = null;
        if (dto.getStudentId() != null) {
            student = studentRepository.findById(dto.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));
        }

        InstructorBankDetail bankDetail = null;
        if (dto.getBankDetailId() != null) {
            bankDetail = bankDetailRepository.findById(dto.getBankDetailId())
                    .orElseThrow(() -> new RuntimeException("Bank detail not found"));
        }

        InstructorEarning earning = mapper.toEntity(dto, instructor, student, bankDetail);
        InstructorEarning saved = earningRepository.save(earning);
        return mapper.toDto(saved);
    }

    public List<InstructorEarningDto> getEarningsByInstructor(UUID instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        return earningRepository.findByInstructor(instructor).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public InstructorEarningDto updateEarningByInstructor(UUID instructorId, InstructorEarningCreationDto dto) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        List<InstructorEarning> earnings = earningRepository.findByInstructor(instructor);

        InstructorEarning target = earnings.stream()
                .filter(e -> e.getAmount().equals(dto.getAmount()) &&
                        e.getPaymentMethod().equals(dto.getPaymentMethod()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Matching earning not found"));

        target.setNotes(dto.getNotes());
        target.setAmount(dto.getAmount());
        target.setPaymentMethod(dto.getPaymentMethod());

        // Optional: update student and bank detail if provided
        if (dto.getStudentId() != null) {
            Student student = studentRepository.findById(dto.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            target.setStudent(student);
        }

        if (dto.getBankDetailId() != null) {
            InstructorBankDetail bankDetail = bankDetailRepository.findById(dto.getBankDetailId())
                    .orElseThrow(() -> new RuntimeException("Bank detail not found"));
            target.setBankDetail(bankDetail);
        }

        InstructorEarning updated = earningRepository.save(target);
        return mapper.toDto(updated);
    }

    public void deleteEarningByInstructor(UUID instructorId, InstructorEarningCreationDto dto) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        List<InstructorEarning> earnings = earningRepository.findByInstructor(instructor);

        InstructorEarning target = earnings.stream()
                .filter(e -> e.getAmount().equals(dto.getAmount()) &&
                        e.getPaymentMethod().equals(dto.getPaymentMethod()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Matching earning not found"));

        earningRepository.delete(target);
    }

    public List<InstructorEarningDto> getEarningsForInstructor(UUID instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        return earningRepository.findByInstructor(instructor)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteEarning(Integer earningId) {
        if(!earningRepository.existsById(earningId)) {
            throw new RuntimeException("Earning not found");
        }
        earningRepository.deleteById(earningId);
    }
}
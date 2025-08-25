package com.tap.services;

import com.tap.dto.InstructorBankDetailCreationDto;
import com.tap.dto.InstructorBankDetailDto;
import com.tap.entities.Instructor;
import com.tap.entities.InstructorBankDetail;
import com.tap.mappers.InstructorBankDetailMapper;
import com.tap.repositories.InstructorBankDetailRepository;
import com.tap.repositories.InstructorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InstructorBankDetailsService {

    private final InstructorBankDetailRepository bankDetailRepository;
    private final InstructorRepository instructorRepository;
    private final InstructorBankDetailMapper mapper;

    public InstructorBankDetailsService(InstructorBankDetailRepository bankDetailRepository,
                                        InstructorRepository instructorRepository,
                                        InstructorBankDetailMapper mapper) {
        this.bankDetailRepository = bankDetailRepository;
        this.instructorRepository = instructorRepository;
        this.mapper = mapper;
    }

    public List<InstructorBankDetailDto> getBankDetailsByInstructorId(UUID instructorId) {
        return bankDetailRepository.findByInstructorUserId(instructorId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public InstructorBankDetailDto addBankDetail(UUID instructorId, InstructorBankDetailCreationDto dto) {
        Instructor instructor = (Instructor) instructorRepository.findByUserId(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        InstructorBankDetail detail = mapper.toEntity(dto, instructor);
        return mapper.toDto(bankDetailRepository.save(detail));
    }

    public InstructorBankDetailDto updateBankDetail(Integer bankDetailId, InstructorBankDetailCreationDto dto) {
        InstructorBankDetail existing = bankDetailRepository.findById(bankDetailId)
                .orElseThrow(() -> new RuntimeException("Bank detail not found"));
        existing.setAccountHolderName(dto.getAccountHolderName());
        existing.setBankName(dto.getBankName());
        existing.setAccountNumber(dto.getAccountNumber());
        existing.setIfscCode(dto.getIfscCode());
        existing.setAccounttype(dto.getAccountType());
        existing.setBranchName(dto.getBranchName());
        return mapper.toDto(bankDetailRepository.save(existing));
    }

    public void deleteBankDetail(Integer bankDetailId) {
        bankDetailRepository.deleteById(bankDetailId);
    }
}
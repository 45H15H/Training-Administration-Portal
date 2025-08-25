package com.tap.repositories;

import com.tap.entities.InstructorBankDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InstructorBankDetailRepository extends JpaRepository<InstructorBankDetail, Integer> {
    List<InstructorBankDetail> findByInstructorUserId(UUID userId);
}
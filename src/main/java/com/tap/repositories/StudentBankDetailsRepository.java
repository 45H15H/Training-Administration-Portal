package com.tap.repositories;

import com.tap.entities.StudentBankDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentBankDetailsRepository extends JpaRepository<StudentBankDetails, Integer> {
    List<StudentBankDetails> findByStudent_UserId(UUID studentId);
    boolean existsByAccountNumber(String accountNumber);
}

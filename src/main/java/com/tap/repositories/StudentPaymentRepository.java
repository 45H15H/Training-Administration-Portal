package com.tap.repositories;

import com.tap.entities.Student;
import com.tap.entities.StudentPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentPaymentRepository extends JpaRepository<StudentPayment,Integer> {
    List<StudentPayment> findByStudent(Student student);
}

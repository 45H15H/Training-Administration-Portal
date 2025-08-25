package com.tap.repositories;

import com.tap.entities.StudentBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentBookingRepository extends JpaRepository<StudentBooking, Long> {
}

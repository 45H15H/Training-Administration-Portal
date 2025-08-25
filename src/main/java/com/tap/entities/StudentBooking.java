package com.tap.entities;

import com.tap.entities.Instructor;
import com.tap.entities.InstructorTimeSlot;
import com.tap.entities.Student;
import jakarta.persistence.*;
import lombok.*;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "StudentBooking", schema = "tap_project")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @ToString.Exclude
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    @ToString.Exclude
    private Instructor instructor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", nullable = false)
    @ToString.Exclude
    private InstructorTimeSlot slot;

    @Column(name = "booked_at",updatable = false)
    private LocalDateTime bookedAt = LocalDateTime.now();

    @Column(name = "status", length = 20)
    private String status = "pending";
}

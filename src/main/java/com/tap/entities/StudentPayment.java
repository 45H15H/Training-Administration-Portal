package com.tap.entities;

import com.tap.entities.Student;
import jakarta.persistence.*;
import lombok.*;
import lombok.ToString;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
// Using snake_case table name to align with Hibernate's physical naming strategy and existing database table
@Table(name = "student_payments", schema = "tap_project")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @ToString.Exclude
    private Student student;

    // Course for which the payment is made
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = true)
    @ToString.Exclude
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_detail_id") // Nullable
    @ToString.Exclude
    private StudentBankDetails studentBankDetails;

    @Column(name = "payment_status", length = 20)
    private String paymentStatus; // Could be an Enum: ('pending', 'completed', 'failed')

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "paid_at")
    private LocalDateTime paidAt = LocalDateTime.now();

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

}

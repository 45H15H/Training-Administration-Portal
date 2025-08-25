package com.tap.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "instructorEarnings", schema = "tap_project")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructorEarning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer earningId;

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    @ToString.Exclude
    private Instructor instructor;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than zero")
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @ToString.Exclude
    private Student student;

    @Column(name = "payment_method", nullable = false, length = 50)
    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    @ManyToOne
    @JoinColumn(name = "bank_detail_id")
    @ToString.Exclude
    private InstructorBankDetail bankDetail;

    @Column(name = "notes", length = 1000)
    @Size(max = 1000, message = "Notes must be 1000 characters or fewer")
    private String notes;

    @Column(name = "received_at", nullable = false)
    @NotNull(message = "Received timestamp is required")
    private LocalDateTime receivedAt;

    @PrePersist
    public void prePersist() {
        if (receivedAt == null) {
            receivedAt = LocalDateTime.now();
        }
    }
}
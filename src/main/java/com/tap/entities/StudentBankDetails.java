package com.tap.entities;

import com.tap.entities.Student;
import jakarta.persistence.*;
import lombok.*;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "studentBankDetails", schema = "tap_project")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentBankDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_detail_id")
    private Integer bankDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @ToString.Exclude
    private Student student;

    @Column(name = "account_holder_name", nullable = false, length = 100)
    private String accountHolderName;

    @Column(name = "bank_name", nullable = false, length = 100)
    private String bankName;

    @Column(name = "account_number", nullable = false, length = 30)
    private String accountNumber;

    @Column(name = "ifsc_code", nullable = false, length = 20)
    private String ifscCode;

    @Column(name = "account_type", length = 20)
    private String accountType; // Could be an Enum: ('savings', 'current')

    @Column(name = "branch_name", nullable = false, length = 100)
    private String branchName;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

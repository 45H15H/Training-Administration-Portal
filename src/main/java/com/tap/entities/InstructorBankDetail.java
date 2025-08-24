package com.tap.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="instructorBankDetails",schema="tap_project")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructorBankDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bankDetailId;

    @Column(name="account_holder_name",nullable = false,length=100)
    @NotBlank(message="Account holder name is required")
    private String accountHolderName;

    @Column(name="bank_name",nullable = false,length = 100)
    @NotBlank(message="Bank name is required")
    private String bankName;

    @Column(name="account_number",nullable = false,length=30,unique = true)
    @NotBlank(message="Account number is required")
    private String accountNumber;

    @Column(name="ifsc_code",nullable = false,length=15)
    @NotBlank(message="IFSC code is required")
    private String ifscCode;

    @Column(name="account_type",nullable = false,length = 20)
    @NotBlank(message="Account type is required")
    private String accounttype;

    @Column(name="branch_name",nullable = false,length = 100)
    @NotBlank(message="Branch name is required")
    private String branchName;

    @Column(name="created_At",nullable = false,updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    @ToString.Exclude
    private Instructor instructor;

}

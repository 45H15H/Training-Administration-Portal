package com.tap.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import java.util.UUID;

@Entity
@Table(name="students",schema="tap_project")
@Data
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "student_id")
public class Student extends User {

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "first name is required")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Formula("first_name || ' ' || last_name")
    @Column(name = "full_name")
    private String fullName;

    @PrePersist
    @PreUpdate
    private void calculateFullName() {
        if (firstName != null && lastName != null) {
            this.fullName = firstName + " " + lastName;
        }
    }

    @Min(value = 15, message = "Age must be at least 15")
    @Max(value = 120, message = "Age must be realistic")
    @Column(name = "age")
    private Integer age;

    @Column(name = "phone_number", nullable = false, unique = true)
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = true;

    public UUID getStudentId() {
        return getUserId(); // inherited from User
    }

}

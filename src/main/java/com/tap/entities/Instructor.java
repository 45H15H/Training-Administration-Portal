package com.tap.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Formula;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "instructors", schema = "tap_project")
@Data
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "instructor_id")
public class Instructor extends User {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name ="last_name", nullable = false)
    private String lastName;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "is_verified")
    @ColumnDefault("false")
    private Boolean isVerified = false;

    @PrePersist
    @PreUpdate
    private void calculateFullName() {
        if (firstName != null && lastName != null) {
            this.fullName = firstName + " " + lastName;
        }
    }

    // relationships
    // one-to-one relationship with the qualifications. one instructor will have one qualificaiton row
    @OneToOne(mappedBy = "instructor", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude
    private InstructorQualification qualification;

    // one-to-many relationship with skills, one instructor can have more than one skills
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude
    private List<InstructorSkill> skills;

    // one-to-many relationship with bank details, one instructor can have more than one bank details
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude
    private List<InstructorBankDetail> bankDetails;

    // one-to-many relationsh with earnings
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<InstructorEarning> earnings;

    // one-to-many relationship with time sltos
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude
    private List<InstructorTimeSlot> timeSlots;

    // one-to-many relationship with courses
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Course> courses;

    public UUID getInstructorId() {
        return getUserId(); // Correctly accesses inherited ID
    }

    public void setBankDetail(InstructorBankDetail detail) {

    }
}

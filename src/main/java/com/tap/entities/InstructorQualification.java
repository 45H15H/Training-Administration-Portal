package com.tap.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "instructorQualifications", schema = "tap_project")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructorQualification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qualification_id")
    private Integer qualificationId;

    @Column(name = "bio")
    private String bio;

    @Column(name = "highest_qualification")
    private String highestQualification;

    @Column(name = "relevant_exp")
    private Integer relevantExperience;

    // one-to-one relationship with instructor, an instructor can have only one bio and all
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    @ToString.Exclude
    private Instructor instructor;

}

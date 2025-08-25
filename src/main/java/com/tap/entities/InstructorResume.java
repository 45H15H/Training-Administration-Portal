package com.tap.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="instructorResumes",schema="tap_project")
public class InstructorResume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer resumeId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="instructor_id")
    @ToString.Exclude
    private Instructor instructor;


    @Column(name="resume_url",nullable = false,length=255,unique = true)
    private String resumeUrl;

    @Column(name="uploaded_at",nullable = false)
    private LocalDateTime uploadedAt;

}



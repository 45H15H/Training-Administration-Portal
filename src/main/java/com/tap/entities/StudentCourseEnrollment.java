package com.tap.entities;

import com.tap.entities.Course;
import com.tap.entities.Student;
import jakarta.persistence.*;
import lombok.*;
import lombok.ToString;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_course_enrollment", schema = "tap_project")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Integer enrollmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @ToString.Exclude
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @ToString.Exclude
    private Course course;

    @Column(name = "enrolled_at", updatable = false)
    private LocalDateTime enrolledAt = LocalDateTime.now();

    @Column(name = "progress", precision = 5, scale = 2)
    private BigDecimal progress;

    @Column(name = "status", length = 20)
    private String status; // Could be an Enum: ('active', 'completed', 'dropped')

}

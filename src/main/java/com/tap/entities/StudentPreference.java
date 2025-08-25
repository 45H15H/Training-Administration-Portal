package com.tap.entities;

import com.tap.entities.Student;
import jakarta.persistence.*;
import lombok.*;
import lombok.ToString;


import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "studentPreferences", schema = "tap_project")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "preference_id")
    private Integer preferenceId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @ToString.Exclude
    private Student student;

    @Column(name = "required_skills", columnDefinition = "text[]")
    private List<String> requiredSkills;

    @Column(name = "preferred_time_slots", columnDefinition = "text[]")
    private List<String> preferredTimeSlots;

    @Column(name = "preferred_start_time")
    private LocalDateTime preferredStartTime;

    @Column(name = "preferred_end_time")
    private LocalDateTime preferredEndTime;
}

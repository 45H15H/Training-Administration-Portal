package com.tap.entities;

import jakarta.persistence.*;
import jakarta.persistence.criteria.Fetch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "instructorSkills", schema = "tap_project")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructorSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instructor_skill_id")
    private Integer instructorSkillId;

    @Column(name = "skill_name", nullable = false)
    private String skillName;

    // many to one relationship with instructors, becasue many skills can be taking by one instructor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    @ToString.Exclude
    private Instructor instructor;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id", nullable = false)
    private ProficiencyLevel level;
}

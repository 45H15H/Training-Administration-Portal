package com.tap.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "proficiencyLevels", schema = "tap_project")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProficiencyLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "level_id")
    private Integer levelId;

    @Enumerated(EnumType.STRING)
    @Column(name = "level_name", nullable = false, unique = true)
    private LevelName levelName;

    // one to many relationship with instructor skills
    // because one level can be taken by many skills
    @OneToMany(mappedBy = "level", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<InstructorSkill> instructorSkills;

    public enum LevelName {
        beginner,
        intermediate,
        advanced
    }
}

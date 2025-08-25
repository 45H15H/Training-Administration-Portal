package com.tap.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name="instrustorTimeSlots",schema="tap_project")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructorTimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer slotId;

    @ManyToOne
    @JoinColumn(name="instructor_id",nullable = false)
    @ToString.Exclude
    private Instructor instructor;

    @Column(name="start_time",nullable = false)
    @NotNull(message="Strat time is required")
    private LocalDateTime startTime;

    @Column(name="end_time",nullable = false)
    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    @Column(name="is_booked",nullable = false)
    private Boolean isBooked=false;


}

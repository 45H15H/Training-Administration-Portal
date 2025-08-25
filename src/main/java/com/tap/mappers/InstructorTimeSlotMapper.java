package com.tap.mappers;

import com.tap.dto.InstructorTimeSlotDto;
import com.tap.entities.Instructor;
import com.tap.entities.InstructorTimeSlot;
import org.springframework.stereotype.Component;

@Component
public class InstructorTimeSlotMapper {

    public InstructorTimeSlot toEntity(InstructorTimeSlotDto dto, Instructor instructor) {
        InstructorTimeSlot slot = new InstructorTimeSlot();
        slot.setSlotId(dto.getSlotId());
        slot.setInstructor(instructor);
        slot.setStartTime(dto.getStartTime());
        slot.setEndTime(dto.getEndTime());
        slot.setIsBooked(dto.getIsBooked() != null ? dto.getIsBooked() : false);
        return slot;
    }

    public InstructorTimeSlotDto toDto(InstructorTimeSlot slot) {
        InstructorTimeSlotDto dto = new InstructorTimeSlotDto();
        dto.setSlotId(slot.getSlotId());
        dto.setInstructorId(slot.getInstructor().getInstructorId());
        dto.setStartTime(slot.getStartTime());
        dto.setEndTime(slot.getEndTime());
        dto.setIsBooked(slot.getIsBooked());
        return dto;
    }
}
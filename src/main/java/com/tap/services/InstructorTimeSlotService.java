package com.tap.services;

import com.tap.dto.InstructorTimeSlotDto;
import com.tap.entities.Instructor;
import com.tap.entities.InstructorTimeSlot;
import com.tap.mappers.InstructorTimeSlotMapper;
import com.tap.repositories.InstructorRepository;
import com.tap.repositories.InstructorTimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InstructorTimeSlotService {

    private final InstructorTimeSlotRepository slotRepository;
    private final InstructorRepository instructorRepository;
    private final InstructorTimeSlotMapper mapper;

    @Autowired
    public InstructorTimeSlotService(
            InstructorTimeSlotRepository slotRepository,
            InstructorRepository instructorRepository,
            InstructorTimeSlotMapper mapper
    ) {
        this.slotRepository = slotRepository;
        this.instructorRepository = instructorRepository;
        this.mapper = mapper;
    }

    public InstructorTimeSlotDto createSlot(UUID instructorId, InstructorTimeSlotDto dto) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Instructor not found"));

        InstructorTimeSlot slot = mapper.toEntity(dto, instructor);
        InstructorTimeSlot saved = slotRepository.save(slot);
        return mapper.toDto(saved);
    }

    public List<InstructorTimeSlotDto> getSlots(UUID instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Instructor not found"));

        return slotRepository.findByInstructor(instructor).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public InstructorTimeSlotDto updateSlot(UUID instructorId, Integer slotId, InstructorTimeSlotDto dto) {
        InstructorTimeSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Slot not found"));

        if (slot.getInstructor() == null || slot.getInstructor().getInstructorId() == null
                || !slot.getInstructor().getInstructorId().equals(instructorId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own slots");
        }

        slot.setStartTime(dto.getStartTime());
        slot.setEndTime(dto.getEndTime());
        slot.setIsBooked(dto.getIsBooked());

        InstructorTimeSlot updated = slotRepository.save(slot);
        return mapper.toDto(updated);
    }

    public void deleteSlot(Integer slotId) {
        if (!slotRepository.existsById(slotId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Slot not found");
        }
        slotRepository.deleteById(slotId);
    }

    public void deleteAllSlots(UUID instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Instructor not found"));

        List<InstructorTimeSlot> slots = slotRepository.findByInstructor(instructor);
        slotRepository.deleteAll(slots);
    }
}
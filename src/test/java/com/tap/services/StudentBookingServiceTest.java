package com.tap.services;

import com.tap.dto.BookingRequestDto;
import com.tap.dto.StudentBookingDto;
import com.tap.entities.*;
import com.tap.exceptions.ResourceNotFoundException;
import com.tap.mappers.UserMapper;
import com.tap.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentBookingServiceTest {

    @Mock
    private StudentBookingRepository bookingRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private InstructorRepository instructorRepository;
    @Mock
    private InstructorTimeSlotRepository timeSlotRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private StudentCourseEnrollmentRepository enrollmentRepository;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private StudentBookingService bookingService;

    private UUID studentId;
    private BookingRequestDto bookingRequest;
    private Student student;
    private Instructor instructor;
    private InstructorTimeSlot timeSlot;
    private Course course;
    private StudentBooking booking;

    @BeforeEach
    void setUpStudentBooking() {
        studentId = UUID.randomUUID();
        UUID instructorId = UUID.randomUUID();
        Integer timeSlotId = 123;
        UUID courseId = UUID.randomUUID();

        bookingRequest = new BookingRequestDto(instructorId, timeSlotId, courseId);

        student = new Student();
        student.setUserId(studentId);

        instructor = new Instructor();
        instructor.setUserId(instructorId);

        timeSlot = new InstructorTimeSlot();
        timeSlot.setSlotId(timeSlotId);
        timeSlot.setInstructor(instructor);
        timeSlot.setIsBooked(false);

        course = new Course();
        course.setCourseId(courseId);
        course.setInstructor(instructor);
        course.setIsPublished(true);

        booking = new StudentBooking();
        booking.setStudent(student);
        booking.setInstructor(instructor);
        booking.setSlot(timeSlot);
        booking.setStatus("CONFIRMED");
    }

    @Test
    void testCreateBooking_Success() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(instructorRepository.findById(bookingRequest.instructorId())).thenReturn(Optional.of(instructor));
        when(timeSlotRepository.findById(bookingRequest.timeSlotId())).thenReturn(Optional.of(timeSlot));
        when(courseRepository.findById(bookingRequest.courseId())).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByStudent_UserIdAndCourse_CourseId(student.getUserId(), course.getCourseId())).thenReturn(false);
        when(bookingRepository.save(any(StudentBooking.class))).thenReturn(booking);
        when(userMapper.toStudentBookingDto(any(StudentBooking.class)))
                .thenReturn(new StudentBookingDto(
                        1,
                        student.getUserId(),
                        "Test Student",
                        instructor.getUserId(),
                        "Test Instructor",
                        timeSlot.getSlotId(),
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(1),
                        "CONFIRMED",
                        LocalDateTime.now()
                ));

        StudentBookingDto result = bookingService.createBooking(studentId, bookingRequest);

        assertNotNull(result);
        verify(timeSlotRepository).save(timeSlot);
        verify(enrollmentRepository).save(any(StudentCourseEnrollment.class));
    }

    @Test
    void testCreateBooking_StudentNotFound() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.createBooking(studentId, bookingRequest));
    }

    @Test
    void testCreateBooking_InstructorNotFound() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(instructorRepository.findById(bookingRequest.instructorId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.createBooking(studentId, bookingRequest));
    }

    @Test
    void testCreateBooking_TimeSlotNotFound() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(instructorRepository.findById(bookingRequest.instructorId())).thenReturn(Optional.of(instructor));
        when(timeSlotRepository.findById(bookingRequest.timeSlotId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.createBooking(studentId, bookingRequest));
    }

    @Test
    void testCreateBooking_CourseNotFound() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(instructorRepository.findById(bookingRequest.instructorId())).thenReturn(Optional.of(instructor));
        when(timeSlotRepository.findById(bookingRequest.timeSlotId())).thenReturn(Optional.of(timeSlot));
        when(courseRepository.findById(bookingRequest.courseId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.createBooking(studentId, bookingRequest));
    }

    @Test
    void testCreateBooking_CourseNotPublished() {
        course.setIsPublished(false);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(instructorRepository.findById(bookingRequest.instructorId())).thenReturn(Optional.of(instructor));
        when(timeSlotRepository.findById(bookingRequest.timeSlotId())).thenReturn(Optional.of(timeSlot));
        when(courseRepository.findById(bookingRequest.courseId())).thenReturn(Optional.of(course));

        assertThrows(ResponseStatusException.class, () -> bookingService.createBooking(studentId, bookingRequest));
    }

    @Test
    void testCreateBooking_CourseInstructorMismatch() {
        Instructor anotherInstructor = new Instructor();
        anotherInstructor.setUserId(UUID.randomUUID());
        course.setInstructor(anotherInstructor);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(instructorRepository.findById(bookingRequest.instructorId())).thenReturn(Optional.of(instructor));
        when(timeSlotRepository.findById(bookingRequest.timeSlotId())).thenReturn(Optional.of(timeSlot));
        when(courseRepository.findById(bookingRequest.courseId())).thenReturn(Optional.of(course));

        assertThrows(ResponseStatusException.class, () -> bookingService.createBooking(studentId, bookingRequest));
    }

    @Test
    void testCreateBooking_TimeSlotInstructorMismatch() {
        Instructor anotherInstructor = new Instructor();
        anotherInstructor.setUserId(UUID.randomUUID());
        timeSlot.setInstructor(anotherInstructor);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(instructorRepository.findById(bookingRequest.instructorId())).thenReturn(Optional.of(instructor));
        when(timeSlotRepository.findById(bookingRequest.timeSlotId())).thenReturn(Optional.of(timeSlot));
        when(courseRepository.findById(bookingRequest.courseId())).thenReturn(Optional.of(course));

        assertThrows(ResponseStatusException.class, () -> bookingService.createBooking(studentId, bookingRequest));
    }

    @Test
    void testCreateBooking_TimeSlotAlreadyBooked() {
        timeSlot.setIsBooked(true);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(instructorRepository.findById(bookingRequest.instructorId())).thenReturn(Optional.of(instructor));
        when(timeSlotRepository.findById(bookingRequest.timeSlotId())).thenReturn(Optional.of(timeSlot));
        when(courseRepository.findById(bookingRequest.courseId())).thenReturn(Optional.of(course));

        assertThrows(ResponseStatusException.class, () -> bookingService.createBooking(studentId, bookingRequest));
    }
}


